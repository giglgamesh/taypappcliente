package pe.oranch.taypappcliente.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pe.oranch.taypappcliente.Config;
import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.activities.EncuentraTuMenuActivity;
import pe.oranch.taypappcliente.adapter.MapPopupAdapter;
import pe.oranch.taypappcliente.listeners.GPSTracker;
import pe.oranch.taypappcliente.models.PItemData;
import pe.oranch.taypappcliente.uis.ProgressWheel;
import pe.oranch.taypappcliente.utilities.Utils;
import pe.oranch.taypappcliente.utilities.VolleySingleton;
import com.rey.material.widget.Slider;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Panacea-Soft on 7/15/15.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class MapFragment extends Fragment {

    /**------------------------------------------------------------------------------------------------
     * Start Block - Private Variables
     **------------------------------------------------------------------------------------------------*/
    private Marker customMarker;
    private LatLng markerLatLng;
    private ProgressWheel progressWheel;
    private ArrayList<PItemData> items;
    private TextView display_message;
    private boolean checkingLatLng = false;
    private HashMap<String, Uri> markerImages = new HashMap<>();
    private HashMap<Marker, PItemData> markerInfo = new HashMap<>();
    private HashMap<String, String> markerAddress = new HashMap<>();
    private int selectedCityId;
    private int selectedSubCatId;
    private double selectedRegionLat;
    private double selectedRegionLng;
    private double currentLongitude;
    private double currentLatitude;
    private View marker;
    private MapView mMapView;
    private String jsonStatusSuccessString;
    private SpannableString connectionErrorString;
    private FrameLayout mainLayout;
    private Picasso p;

    /**------------------------------------------------------------------------------------------------
     * End Block - Private Variables
     **------------------------------------------------------------------------------------------------*/

    public MapFragment(){

    }

    /**------------------------------------------------------------------------------------------------
     * Start Block - Override Functions
     **------------------------------------------------------------------------------------------------*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        initData();

        initUI(v, inflater, container, savedInstanceState);

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mMapView.onResume();
        }catch (Exception e){
            Utils.psErrorLog("Error in map view resume . " , e);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            mMapView.onPause();
        }catch(Exception e){
            Utils.psErrorLog("Error in map view pause . " , e);
        }
    }

    @Override
    public void onDestroy() {
        try {
            customMarker = null;
            items.clear();
            markerImages.clear();
            markerInfo.clear();
            mMapView.onDestroy();
            //p.shutdown();
            Utils.unbindDrawables(mainLayout);
            super.onDestroy();
        }catch (Exception e){
            super.onDestroy();
        }
    }


    @Override
    public void onLowMemory() {
        try {
            super.onLowMemory();
            System.gc();
            mMapView.onLowMemory();
        }catch (Exception e){
            Utils.psErrorLog("Error in onLowMemory.", e);
        }
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Override Functions
     **------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------
     * Start Block - INit data Functions
     **------------------------------------------------------------------------------------------------*/
    private void initData() {

        try {
            jsonStatusSuccessString = getResources().getString(R.string.json_status_success);
            connectionErrorString = Utils.getSpannableString(getString(R.string.connection_error));

            p =new Picasso.Builder(getActivity())
                    .memoryCache(new LruCache(1))
                    .build();
        }catch(Exception e){
            Utils.psErrorLogE("Error in init data.", e);
        }

    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Init Data Functions
     **------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------
     * Start Block - Init UI Functions
     **------------------------------------------------------------------------------------------------*/
    private void initUI(View v, LayoutInflater inflater, ViewGroup container,
                        Bundle savedInstanceState){
        if (Utils.isGooglePlayServicesOK(getActivity())) {
            Utils.psLog("Google Play Service is ready for Google Map");


            initFAB(v, container);
            initMessage(v);
            loadPreferenceData();

            loadMap(v, savedInstanceState, inflater, container);
            mainLayout = (FrameLayout) v.findViewById(R.id.main_layout);

            System.gc();
            //mMapView.getMap().clear();
        } else {
            showNoServicePopup();
        }
    }

    private void initFAB(View v,final ViewGroup parent) {
        FloatingActionButton locationSearchFAB = (FloatingActionButton) v.findViewById(R.id.location_search_fab);

        locationSearchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkingLatLng) {
                    if (readyLatLng()) {
                        showSearchPopup(parent);
                    } else {
                        showWaitPopup();
                    }
                } else {

                    showSearchPopup(parent);

                }
            }
        });

    }

    private void initMessage(View v) {
        display_message = (TextView) v.findViewById(R.id.display_message);
        display_message.setVisibility(View.GONE);

        progressWheel = (ProgressWheel) v.findViewById(R.id.progress_wheel);
        progressWheel.setVisibility(View.VISIBLE);
    }

    private void loadMap(View v, Bundle savedInstanceState, LayoutInflater inflater, ViewGroup container) {
        try {
            mMapView = (MapView) v.findViewById(R.id.mapView);

            ViewGroup.LayoutParams params = mMapView.getLayoutParams();


            mMapView.onCreate(savedInstanceState);

            mMapView.onResume();

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            marker = inflater.inflate(R.layout.custom_marker, container, false);
            //NO SE EXACTAMENTE PARA QUE ES (MODIFICAR - DANIEL)
            requestData(Config.APP_API_URL2 + Config.ITEMS_BY_SUB_CATEGORY + selectedCityId + "/sub_cat_id/" + selectedSubCatId + "/item/all/", marker);
        }catch (Exception e){
            Utils.psErrorLog("Error in Load Map.", e);
        }

    }

    private void loadPreferenceData() {
        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
            selectedCityId = pref.getInt("_selected_city_id", 0);
            selectedSubCatId = pref.getInt("_selected_sub_cat_id", 0);
            selectedRegionLat = Double.parseDouble(pref.getString("_city_region_lat", ""));
            selectedRegionLng = Double.parseDouble(pref.getString("_city_region_lng", ""));
        }catch (Exception e){
            Utils.psErrorLog("Error in load preference data. ",e);
        }
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - INit UI Functions
     **------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------
     * Start Block - Private Functions
     **------------------------------------------------------------------------------------------------*/
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Current Address ( ");
                Utils.psLog("Getting Address.");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    if (i != returnedAddress.getMaxAddressLineIndex() - 1) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    } else {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("");
                    }
                }
                strReturnedAddress.append(" )");
                strAdd = strReturnedAddress.toString();
                Utils.psLog("My loction address --- " + "" + strReturnedAddress.toString());
            } else {
                Utils.psLog("My Current loction address" + "No Address returned!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Utils.psLog("My Current loction address >>" + e.getMessage());
        }
        return strAdd;
    }

    private void requestData(String uri, final View marker) {
        JsonObjectRequest request = new JsonObjectRequest(uri,

                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals(jsonStatusSuccessString)) {

                                progressWheel.setVisibility(View.GONE);
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<PItemData>>() {
                                }.getType();
                                items = gson.fromJson(response.getString("data"), listType);

                                mMapView.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(final GoogleMap googleMap) {
                                        for (PItemData itd : items) {

                                            if (itd != null) {
                                                double latitude = Double.parseDouble(itd.lat);
                                                double longitude = Double.parseDouble(itd.lng);

                                                markerLatLng = new LatLng(latitude, longitude);

                                                customMarker = googleMap.addMarker(new MarkerOptions()
                                                        .position(markerLatLng)
                                                        .title(itd.name.toUpperCase())
                                                        .snippet(itd.description.substring(0, Math.min(itd.description.length(), 15)) + "...")
                                                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), marker)))
                                                        .anchor(0.5f, 1));
                                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                                        .target(new LatLng(selectedRegionLat, selectedRegionLng)).zoom(13).build();
                                                googleMap.animateCamera(CameraUpdateFactory
                                                        .newCameraPosition(cameraPosition));


                                                if (markerImages != null) {
                                                    //FUNCION PARA BUSCAR BASADO EN GEOLOCALIZACION (MODIFICAR ESTA PARTE- DANIEL )
                                                    markerImages.put(customMarker.getId(), Uri.parse(Config.APP_IMAGES_URL + itd.images.get(0).path));
                                                }

                                                if (markerInfo != null) {
                                                    markerInfo.put(customMarker, itd);
                                                }

                                                if (markerAddress != null) {
                                                    markerAddress.put(customMarker.getId(), itd.address.substring(0, Math.min(itd.address.length(), 15)) + "...");
                                                }

                                                googleMap.setInfoWindowAdapter(new MapPopupAdapter(getActivity(), getActivity().getLayoutInflater(), markerImages, markerAddress, p));
                                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                    @Override
                                                    public boolean onMarkerClick(Marker marker) {
                                                        marker.showInfoWindow();
                                                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                                                        return true;
                                                    }
                                                });

                                                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                    @Override
                                                    public void onInfoWindowClick(Marker marker) {

                                                        PItemData ct = markerInfo.get(marker);
                                                        Utils.psLog("Selected Item Name : " + ct.name);
                                                        final Intent intent;
                                                        //PARA IR AL RESTAURANTE QUE SE VE EN EL MAPA (MODIFICAR ESTA PARTE- DANIEL )
                                                        intent = new Intent(getActivity(), EncuentraTuMenuActivity.class);
                                                        intent.putExtra("selected_item_id", ct.id);
                                                        intent.putExtra("selected_city_id", selectedCityId);
                                                        startActivity(intent);

                                                    }
                                                });
                                            }
                                        }
                                    }
                                });


                            } else {

                                Utils.psLog("Error in loading.");
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError ex) {
                        Log.d(">> Volley Error ", ex.getMessage() + "");
                        progressWheel.setVisibility(View.GONE);


                        NetworkResponse response = ex.networkResponse;
                        if (response == null || response.data == null) {
                            display_message.setVisibility(View.VISIBLE);
                            display_message.setText(connectionErrorString);

                        }

                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setShouldCache(false);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Private Functions
     **------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------
     * Start Block - Public Functions
     **------------------------------------------------------------------------------------------------*/
    public void showSearchPopup(ViewGroup parent) {
        checkingLatLng = false;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(R.string.location_search_title);
        LayoutInflater inflater = (getActivity()).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.slider, parent, false);
        dialogBuilder.setView(dialogView);

        Button BtnSearch = (Button) dialogView.findViewById(R.id.button_search);
        final TextView addressTextView = (TextView) dialogView.findViewById(R.id.complete_address);
        getCurrentLocation(addressTextView);
        final Slider slider = (Slider) dialogView.findViewById(R.id.location_slider);

        final AlertDialog alert = dialogBuilder.create();
        alert.show();
        BtnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alert.dismiss();

                if(slider != null) {
                    Utils.psLog(String.valueOf(slider.getValue()));
                    //FUNCION PARA BUSCAR BASADO EN GEOLOCALIZACION (MODIFICAR ESTA PARTE- DANIEL )
                    Utils.psLog(Config.APP_API_URL2 + Config.SEARCH_BY_GEO + slider.getValue() + "/userLat/" + currentLatitude + "/userLong/" + currentLongitude + "/city_id/" + selectedCityId + "/sub_cat_id/" + selectedSubCatId);
                    requestData(Config.APP_API_URL2 + Config.SEARCH_BY_GEO + slider.getValue() + "/userLat/" + currentLatitude + "/userLong/" + currentLongitude + "/city_id/" + selectedCityId + "/sub_cat_id/" + selectedSubCatId, marker);
                }

            }
        });

    }

    public void showWaitPopup() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.pls_wait);
        builder.setMessage(R.string.gps_not_ready);
        builder.setPositiveButton(R.string.OK, null);
        builder.show();
    }

    public void showNoServicePopup() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.sorry_title);
        builder.setMessage(R.string.no_google_play);
        builder.setPositiveButton(R.string.OK, null);
        builder.show();
    }

    public boolean readyLatLng() {
        GPSTracker gps = new GPSTracker(getActivity());

        if (gps.canGetLocation()) {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
            return currentLatitude != 0.0 && currentLongitude != 0.0;

        } else {
            return false;
        }

    }

    public void getCurrentLocation(TextView tv) {

        GPSTracker gps = new GPSTracker(getActivity());

        if (gps.canGetLocation()) {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();

        } else {
            gps.showSettingsAlert();
            checkingLatLng = true;
        }

        tv.setText(getCompleteAddressString(currentLatitude, currentLongitude));

    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        view.destroyDrawingCache();

        return bitmap;
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Public Functions
     **------------------------------------------------------------------------------------------------*/


}

package pe.oranch.taypappcliente.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pe.oranch.taypappcliente.Config;
import pe.oranch.taypappcliente.GlobalData;
import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.activities.DescubrirActivity;
import pe.oranch.taypappcliente.adapter.Tay_restaurantesAdapter;
import pe.oranch.taypappcliente.models.PCartaData;
import pe.oranch.taypappcliente.models.PComidaData;
import pe.oranch.taypappcliente.models.PMenuFotoData;
import pe.oranch.taypappcliente.models.PMenuFotoFotoData;
import pe.oranch.taypappcliente.models.PRestauranteData;
import pe.oranch.taypappcliente.models.RestauranteRowData;
import pe.oranch.taypappcliente.utilities.CacheRequest;
import pe.oranch.taypappcliente.utilities.Utils;
import pe.oranch.taypappcliente.utilities.VolleySingleton;

import static pe.oranch.taypappcliente.Config.APP_API_CARTA;

/**
 * Created by Daniel on 27/01/2018.
 */

public class DialogMenuFotoFragment extends DialogFragment {
    TextView cerrardialog;
    TextView detalledescuento, contenidodialog, fechamenufoto;
    ImageView fotomenu;
    //Nuevas Variables
    private String jsonStatusSuccessString;
    private ArrayList<PMenuFotoData> pMenuFotoDataList;
    private ArrayList<PMenuFotoData> pMenuFotoDataSet;
    private PMenuFotoData pMenuFoto;
    private int selectedMenuFotoID;

    public static DialogMenuFotoFragment newInstance() {
        DialogMenuFotoFragment frag = new DialogMenuFotoFragment();

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_menu_foto, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        // Set ur code
        initUI(view);
        initData(view);
        //initDataFoto();
        //loadMenuFotoFotoGrid(view);
        //initData(view);
        return builder.create();
    }

    @Override
    public void onDestroy() {

        try {
            GlobalData.menufotodata = null;
            super.onDestroy();
        }catch (Exception e){
            super.onDestroy();
        }
    }

    private void initUI(View view) {
        cerrardialog = (TextView) view.findViewById(R.id.CerrarDialog);
        cerrardialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initData(View view){
        try {
            String id_empresa= getArguments().getString("id_empresa");
            requestData((Config.APP_API_URL + Config.GET_MENU_FOTO + id_empresa), view);
            jsonStatusSuccessString = getResources().getString(R.string.json_status_success);
        }catch (Exception e){
            Utils.psErrorLogE("Error in bind Rating", e);
        }
    }

    private void initDataFoto(View view){
        try {
            GlobalData.menufotodata = pMenuFotoDataList.get(0);
            pMenuFoto = GlobalData.menufotodata;
            selectedMenuFotoID = pMenuFoto.tay_menu_dia_id;
            loadMenuFotoFotoGrid(view);
        }catch (Exception e){
            Utils.psErrorLogE("Error in bind Rating", e);
        }
    }

    public void loadMenuFotoFotoGrid(View view) {
        try {
            fechamenufoto = (TextView) view.findViewById(R.id.FechaMenuFoto);
            fotomenu = (ImageView) view.findViewById(R.id.FotoMenu);
            for(PMenuFotoFotoData cd : pMenuFoto.menufoto) {
                fechamenufoto.setText("Publicado el "+cd.tay_menu_dia_fecha);
                String urlimagen;
                urlimagen = APP_API_CARTA + (cd.tay_menu_dia_ruta);
                Picasso.with(getActivity().getApplicationContext()).load(urlimagen).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(fotomenu);
            }

        } catch (Resources.NotFoundException e) {
            Utils.psErrorLogE("Error in loadCategoryGrid.", e);
        }
    }

    private void requestData(String uri, final View view) {
        CacheRequest cacheRequest = new CacheRequest(0, uri, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);

                    String status = jsonObject.getString("status");
                    if (status.equals(jsonStatusSuccessString)) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<PMenuFotoData>>() {
                        }.getType();

                        pMenuFotoDataList = gson.fromJson(jsonObject.getString("data"), listType);


                        if(pMenuFotoDataList != null && pMenuFotoDataList.size() > 1) {
                            updateDisplay();
                        }
                        updateGlobalMenuFotoList();
                        initDataFoto(view);
                    } else {
                        Utils.psLog("Error in loading MenuFoto.");
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {

                }catch (Exception e){
                    Utils.psErrorLogE("Error in Connection Url.", e);
                }
            }
        });

        cacheRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(cacheRequest);
    }

    private void updateDisplay() {
        for (PMenuFotoData cd : pMenuFotoDataList) {
            pMenuFotoDataSet.add(cd);
        }
    }

    private void updateGlobalMenuFotoList() {
        GlobalData.menufotoDatas.clear();

        if(pMenuFotoDataList != null) {
            for (int i = 0; i < pMenuFotoDataList.size(); i++) {
                GlobalData.menufotoDatas.add(pMenuFotoDataList.get(i));
            }
        }
    }
}

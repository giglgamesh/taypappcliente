package pe.oranch.taypappcliente.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import pe.oranch.taypappcliente.BottomNavigationViewHelper;
import pe.oranch.taypappcliente.Config;
import pe.oranch.taypappcliente.GlobalData;
import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.entidades.Tay_slider;
import pe.oranch.taypappcliente.recursos.PrefManager;
import pe.oranch.taypappcliente.utilities.Utils;
import pe.oranch.taypappcliente.utilities.VolleySingleton;

import static pe.oranch.taypappcliente.Config.APP_IMAGES_LOCAL;

public class CalificacionActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {
    CalificacionActivity restauranteActivity;
    //PARA EL REFRESH LAYOUT
    SwipeRefreshLayout swipeRefreshLayout;
    //FIN REFRESH LAYOUT
    //Nuevas Variables
    private String jsonStatusSuccessString;
    private String connectionError;
    private SliderLayout mDemoSlider;
    HashMap<String, String> HashMapForURL ;
    ArrayList<Tay_slider> listaSlider;
    String textoslider;
    String urlimagen;
    String nombreslider;
    String restaurante_comida,restaurante_nombre,restaurante_direccion, restaurante_telefono, restaurante_inicio, restaurante_fin, restaurante_latitud, restaurante_longitud, restaurante_id;
    String valorratingsabor, valorratingambiente, valorratingservicio;
    TextView nombrerestaurante;    //variable del archivo de sesion
    RatingBar ratingsabor, ratingambiente, ratingservicio;
    private SharedPreferences pref;
    //VARIABLES RECORDAR SESION
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificacion);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_informacion:
                        Intent intentReg = new Intent(CalificacionActivity.this,RestauranteActivity.class);
                        CalificacionActivity.this.startActivity(intentReg);
                        break;
                    case R.id.ic_comollegar:
                        pref = PreferenceManager.getDefaultSharedPreferences(CalificacionActivity.this.getBaseContext());
                        restaurante_latitud = pref.getString("restaurante_latitud","");
                        restaurante_longitud = pref.getString("restaurante_longitud","");

                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+restaurante_latitud+","+restaurante_longitud);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                        break;
                    case R.id.ic_calificaciones:
                        //Intent intentReg3 = new Intent(RestauranteActivity.this,OfertaActivity.class);
                        //RestauranteActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_comentarios:
                        Intent intentReg4 = new Intent(CalificacionActivity.this,ComentariosActivity.class);
                        CalificacionActivity.this.startActivity(intentReg4);
                        break;
                }
                return false;
            }
        });

        initUI();
        initData();
    }
    @Override
    public void onDestroy() {

        try {
            swipeRefreshLayout = null;
            //p.shutdown();
            GlobalData.comidadata = null;
            super.onDestroy();
        }catch (Exception e){
            super.onDestroy();
        }
    }

    private void initUI (){
        pref = PreferenceManager.getDefaultSharedPreferences(CalificacionActivity.this.getBaseContext());
        restaurante_comida = pref.getString("restaurante_comida","");
        restaurante_nombre = pref.getString("restaurante_nombre","");
        restaurante_direccion = pref.getString("restaurante_direccion","");
        restaurante_telefono = pref.getString("restaurante_telefono","");
        restaurante_inicio = pref.getString("restaurante_inicio","");
        restaurante_fin = pref.getString("restaurante_fin","");
        restaurante_id= pref.getString("restaurante_id","");

        //INICIALIZAR REFRESH LAYOUT
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        //FIN REFRESH LAYOUT
        //REFRESH
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
                actualizarComidas();
            }
        });
    }

    public void actualizarComidas(){
        mDemoSlider.removeAllSliders();
        initData();
    }

    public void initData(){
        try {
            nombrerestaurante = findViewById(R.id.NombreRestaurante);
            nombrerestaurante.setText(restaurante_nombre);

            jsonStatusSuccessString = getResources().getString(R.string.json_status_success);
            final String URL = Config.APP_API_URL + Config.GET_FOTOS_SLIDER;
            final String URLRATINGSABOR = Config.APP_API_URL + Config.GET_RATINGS + restaurante_id+"/1";
            final String URLRATINGAMBIENTE = Config.APP_API_URL + Config.GET_RATINGS + restaurante_id+"/2";
            final String URLRATINGSERVICIO = Config.APP_API_URL + Config.GET_RATINGS + restaurante_id+"/3";
            Utils.psLog(URL);
            getSlider(URL);
            getRatingSabor(URLRATINGSABOR);
            getRatingAmbiente(URLRATINGAMBIENTE);
            getRatingServicio(URLRATINGSERVICIO);
        }catch (Exception e){
            Utils.psErrorLogE("Error in bind Rating", e);
        }
    }
    private void getRatingSabor(String postURL){
        ratingsabor = (RatingBar) findViewById(R.id.RatingSabor);
        if (isInternetOn()) {
            JsonObjectRequest req = new JsonObjectRequest(postURL,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String success_status = response.getString("status");
                                if (success_status.equals(jsonStatusSuccessString)) {
                                    JSONArray datjson = response.optJSONArray("data");
                                    for (int i = 0; i < datjson.length(); i++) {
                                        JSONObject jsonObject = null;
                                        jsonObject = datjson.getJSONObject(i);
                                        if (jsonObject.optString("rating_promedio")==null){
                                            valorratingsabor = "0";
                                        }else{
                                            valorratingsabor = jsonObject.optString("rating_promedio");
                                        }
                                        ratingsabor.setRating(Float.parseFloat(valorratingsabor));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            req.setShouldCache(false);
            VolleySingleton.getInstance(this).addToRequestQueue(req);

        } else {
            showOffline();
        }
    }
    private void getRatingAmbiente(String postURL){
        ratingambiente = (RatingBar) findViewById(R.id.RatingAmbiente);
        if (isInternetOn()) {
            JsonObjectRequest req = new JsonObjectRequest(postURL,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String success_status = response.getString("status");
                                if (success_status.equals(jsonStatusSuccessString)) {
                                    JSONArray datjson = response.optJSONArray("data");
                                    for (int i = 0; i < datjson.length(); i++) {
                                        JSONObject jsonObject = null;
                                        jsonObject = datjson.getJSONObject(i);
                                        if (jsonObject.optString("rating_promedio")==null){
                                            valorratingambiente = "0";
                                        }else{
                                            valorratingambiente = jsonObject.optString("rating_promedio");
                                        }
                                        ratingambiente.setRating(Float.parseFloat(valorratingambiente));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            req.setShouldCache(false);
            VolleySingleton.getInstance(this).addToRequestQueue(req);

        } else {
            showOffline();
        }
    }
    private void getRatingServicio(String postURL){
        ratingservicio = (RatingBar) findViewById(R.id.RatingServicio);
        if (isInternetOn()) {
            JsonObjectRequest req = new JsonObjectRequest(postURL,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String success_status = response.getString("status");
                                if (success_status.equals(jsonStatusSuccessString)) {
                                    JSONArray datjson = response.optJSONArray("data");
                                    for (int i = 0; i < datjson.length(); i++) {
                                        JSONObject jsonObject = null;
                                        jsonObject = datjson.getJSONObject(i);
                                        if (jsonObject.optString("rating_promedio")==null){
                                            valorratingservicio = "0";
                                        }else{
                                            valorratingservicio = jsonObject.optString("rating_promedio");
                                        }
                                        ratingservicio.setRating(Float.parseFloat(valorratingservicio));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            req.setShouldCache(false);
            VolleySingleton.getInstance(this).addToRequestQueue(req);

        } else {
            showOffline();
        }
    }
    private void getSlider(String postURL) {
        if (isInternetOn()) {
            mDemoSlider = (SliderLayout) findViewById(R.id.slider);
            HashMapForURL = new HashMap<String, String>();
            JsonObjectRequest req = new JsonObjectRequest(postURL,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                listaSlider = new ArrayList<>();
                                Tay_slider tay_slider = null;
                                String success_status = response.getString("status");
                                if (success_status.equals(jsonStatusSuccessString)) {
                                    JSONArray datjson = response.optJSONArray("data");
                                    for (int i = 0; i < datjson.length(); i++) {
                                        tay_slider = new Tay_slider();
                                        JSONObject jsonObject = null;
                                        jsonObject = datjson.getJSONObject(i);
                                        tay_slider.setTay_slider_id(jsonObject.optInt("tay_slider_id"));
                                        tay_slider.setTay_slider_nombre(jsonObject.optString("tay_slider_nombre"));
                                        tay_slider.setTay_slider_url(jsonObject.optString("tay_slider_url"));
                                        listaSlider.add(tay_slider);

                                        urlimagen = APP_IMAGES_LOCAL + (listaSlider.get(i).getTay_slider_url().toString());
                                        textoslider = listaSlider.get(i).getTay_slider_nombre().toString();
                                        nombreslider = listaSlider.get(i).getTay_slider_id().toString();
                                        HashMapForURL.put(textoslider, urlimagen);


                                        TextSliderView textSliderView = new TextSliderView(CalificacionActivity.this);
                                        textSliderView
                                                .description(textoslider)
                                                .image(HashMapForURL.get(textoslider))
                                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                                .setOnSliderClickListener(CalificacionActivity.this);
                                        textSliderView.bundle(new Bundle());
                                        textSliderView.getBundle()
                                                .putString("extra", textoslider);
                                        mDemoSlider.addSlider(textSliderView);
                                    }

                                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

                                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

                                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());

                                    mDemoSlider.setDuration(3000);

                                    mDemoSlider.addOnPageChangeListener(CalificacionActivity.this);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            req.setShouldCache(false);
            VolleySingleton.getInstance(this).addToRequestQueue(req);

        } else {
            showOffline();
        }
    }
    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void VolverPerfil(View view) {
        onBackPressed();
    }

    private void stopLoading(){
        try {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }catch (Exception e){}
    }

    public final boolean isInternetOn() {

        ConnectivityManager cm = (ConnectivityManager) CalificacionActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    private void showOffline() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(CalificacionActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.sorry_title);
        builder.setMessage(R.string.device_offline);
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.psLog("OK clicked.");
            }
        });
        builder.show();
    }
    public void VerMapa(View view) {
        Intent intent = new Intent(CalificacionActivity.this,MapaActivity.class);
        CalificacionActivity.this.startActivity(intent);
    }
}

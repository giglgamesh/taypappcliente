package pe.oranch.taypappcliente.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pe.oranch.taypappcliente.BottomNavigationViewHelper;
import pe.oranch.taypappcliente.Config;
import pe.oranch.taypappcliente.GlobalData;
import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.adapter.Tay_comidaAdapter;
import pe.oranch.taypappcliente.entidades.Tay_comida;
import pe.oranch.taypappcliente.entidades.Tay_slider;
import pe.oranch.taypappcliente.listeners.ClickListener;
import pe.oranch.taypappcliente.listeners.RecyclerTouchListener;
import pe.oranch.taypappcliente.models.PComidaData;
import pe.oranch.taypappcliente.recursos.PrefManager;
import pe.oranch.taypappcliente.utilities.CacheRequest;
import pe.oranch.taypappcliente.utilities.Utils;
import pe.oranch.taypappcliente.utilities.VolleySingleton;

import static pe.oranch.taypappcliente.Config.APP_IMAGES_LOCAL;

public class RestauranteActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {
    RestauranteActivity restauranteActivity;
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
    String restaurante_comida,restaurante_nombre,restaurante_direccion, restaurante_telefono, restaurante_inicio, restaurante_fin, restaurante_latitud, restaurante_longitud;
    TextView textocomida, textodireccion, textotelefono, textohorario, nombrerestaurante;    //variable del archivo de sesion
    private SharedPreferences pref;
    //VARIABLES RECORDAR SESION
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_informacion:

                        break;
                    case R.id.ic_comollegar:
                        pref = PreferenceManager.getDefaultSharedPreferences(RestauranteActivity.this.getBaseContext());
                        restaurante_latitud = pref.getString("restaurante_latitud","");
                        restaurante_longitud = pref.getString("restaurante_longitud","");

                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+restaurante_latitud+","+restaurante_longitud);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                        break;
                    case R.id.ic_calificaciones:
                        Intent intentReg3 = new Intent(RestauranteActivity.this,CalificacionActivity.class);
                        RestauranteActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_comentarios:
                        Intent intentReg4 = new Intent(RestauranteActivity.this,ComentariosActivity.class);
                        RestauranteActivity.this.startActivity(intentReg4);
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
        pref = PreferenceManager.getDefaultSharedPreferences(RestauranteActivity.this.getBaseContext());
        restaurante_comida = pref.getString("restaurante_comida","");
        restaurante_nombre = pref.getString("restaurante_nombre","");
        restaurante_direccion = pref.getString("restaurante_direccion","");
        restaurante_telefono = pref.getString("restaurante_telefono","");
        restaurante_inicio = pref.getString("restaurante_inicio","");
        restaurante_fin = pref.getString("restaurante_fin","");

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
            textocomida = findViewById(R.id.textoComida);
            textodireccion = findViewById(R.id.textoDireccion);
            textotelefono = findViewById(R.id.textoTelefono);
            textohorario = findViewById(R.id.textoHorario);
            nombrerestaurante = findViewById(R.id.NombreRestaurante);

            textocomida.setText(restaurante_comida);
            textodireccion.setText(restaurante_direccion);
            nombrerestaurante.setText(restaurante_nombre);
            if (restaurante_telefono.equals("N/D")){
                textotelefono.setText("N/D");
            }else{
                textotelefono.setText(restaurante_telefono);
            }
            if (restaurante_inicio.equals("N/D") || restaurante_fin.equals("N/D") ){
                textohorario.setText("N/D");
            }else{
                //transofrmador de horario
                SimpleDateFormat _24Horas = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12Horas = new SimpleDateFormat("hh:mm a");
                Date _24HorasInicialDt = _24Horas.parse(restaurante_inicio);
                Date _24HorasFinalDt = _24Horas.parse(restaurante_fin);
                //fin transformacion horario
                textohorario.setText("Horarios de atención: "+_12Horas.format(_24HorasInicialDt)+" - "+_12Horas.format(_24HorasFinalDt));
            }
            jsonStatusSuccessString = getResources().getString(R.string.json_status_success);
            final String URL = Config.APP_API_URL + Config.GET_FOTOS_SLIDER;
            Utils.psLog(URL);
            getSlider(URL);
        }catch (Exception e){
            Utils.psErrorLogE("Error in bind Rating", e);
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


                                        TextSliderView textSliderView = new TextSliderView(RestauranteActivity.this);
                                        textSliderView
                                                .description(textoslider)
                                                .image(HashMapForURL.get(textoslider))
                                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                                .setOnSliderClickListener(RestauranteActivity.this);
                                        textSliderView.bundle(new Bundle());
                                        textSliderView.getBundle()
                                                .putString("extra", textoslider);
                                        mDemoSlider.addSlider(textSliderView);
                                    }

                                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

                                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

                                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());

                                    mDemoSlider.setDuration(3000);

                                    mDemoSlider.addOnPageChangeListener(RestauranteActivity.this);
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

        ConnectivityManager cm = (ConnectivityManager) RestauranteActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                new AlertDialog.Builder(RestauranteActivity.this, R.style.AppCompatAlertDialogStyle);
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
        Intent intent = new Intent(RestauranteActivity.this,MapaActivity.class);
        RestauranteActivity.this.startActivity(intent);
    }
}

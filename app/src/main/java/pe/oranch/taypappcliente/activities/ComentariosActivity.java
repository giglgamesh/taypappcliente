package pe.oranch.taypappcliente.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pe.oranch.taypappcliente.BottomNavigationViewHelper;
import pe.oranch.taypappcliente.Config;
import pe.oranch.taypappcliente.GlobalData;
import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.adapter.Tay_cartaAdapter;
import pe.oranch.taypappcliente.adapter.Tay_comentariosAdapter;
import pe.oranch.taypappcliente.entidades.Tay_slider;
import pe.oranch.taypappcliente.models.PCartaData;
import pe.oranch.taypappcliente.models.PComentarioData;
import pe.oranch.taypappcliente.recursos.PrefManager;
import pe.oranch.taypappcliente.utilities.CacheRequest;
import pe.oranch.taypappcliente.utilities.Utils;
import pe.oranch.taypappcliente.utilities.VolleySingleton;

import static pe.oranch.taypappcliente.Config.APP_IMAGES_LOCAL;

public class ComentariosActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {
    ComentariosActivity restauranteActivity;
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
    TextView nombrerestaurante;    //variable del archivo de sesion
    private SharedPreferences pref;
    //VARIABLES RECORDAR SESION
    private PrefManager prefManager;
    private ArrayList<PComentarioData> pComentarioDataList;
    private ArrayList<PComentarioData> pComentarioDataSet;
    private Tay_comentariosAdapter adapter;
    RecyclerView idrecyclerlista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_informacion:
                        Intent intentReg = new Intent(ComentariosActivity.this,RestauranteActivity.class);
                        ComentariosActivity.this.startActivity(intentReg);
                        break;
                    case R.id.ic_comollegar:
                        pref = PreferenceManager.getDefaultSharedPreferences(ComentariosActivity.this.getBaseContext());
                        restaurante_latitud = pref.getString("restaurante_latitud","");
                        restaurante_longitud = pref.getString("restaurante_longitud","");

                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+restaurante_latitud+","+restaurante_longitud);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                        break;
                    case R.id.ic_calificaciones:
                        Intent intentReg3 = new Intent(ComentariosActivity.this,CalificacionActivity.class);
                        ComentariosActivity.this.startActivity(intentReg3);
                        break;
                    case R.id.ic_comentarios:
                        //Intent intentReg4 = new Intent(RestauranteActivity.this,PerfilActivity.class);
                        //RestauranteActivity.this.startActivity(intentReg4);
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
            idrecyclerlista = null;
            swipeRefreshLayout = null;
            //p.shutdown();
            GlobalData.comentariodata = null;
            super.onDestroy();
        }catch (Exception e){
            super.onDestroy();
        }
    }
    public void IniciarObjetos(){
        idrecyclerlista = findViewById(R.id.idRecyclerLista);
        idrecyclerlista.setLayoutManager(new LinearLayoutManager(ComentariosActivity.this));
        idrecyclerlista.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(ComentariosActivity.this);
        idrecyclerlista.setLayoutManager(llm);

        pComentarioDataSet = new ArrayList<>();
        adapter = new Tay_comentariosAdapter(ComentariosActivity.this, pComentarioDataSet);
        idrecyclerlista.setAdapter(adapter);
    }

    private void initUI (){
        pref = PreferenceManager.getDefaultSharedPreferences(ComentariosActivity.this.getBaseContext());
        restaurante_comida = pref.getString("restaurante_comida","");
        restaurante_nombre = pref.getString("restaurante_nombre","");
        restaurante_direccion = pref.getString("restaurante_direccion","");
        restaurante_telefono = pref.getString("restaurante_telefono","");
        restaurante_inicio = pref.getString("restaurante_inicio","");
        restaurante_fin = pref.getString("restaurante_fin","");
        restaurante_id= pref.getString("restaurante_id","");

        IniciarObjetos();
        initSwipeRefreshLayout();
    }
    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(Config.APP_API_URL + Config.GET_COMENTARIOS+restaurante_id);
            }
        });
    }

    private void requestData(String uri) {
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
                        Type listType = new TypeToken<List<PComentarioData>>() {
                        }.getType();

                        pComentarioDataList = gson.fromJson(jsonObject.getString("data"), listType);


                        if(pComentarioDataList != null && pComentarioDataList.size() > 1) {
                            idrecyclerlista.setVisibility(View.VISIBLE);
                            updateDisplay();
                        }
                        updateGlobalComentarioList();
                    } else {
                        stopLoading();
                        Utils.psLog("Error in loading CityList.");
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    stopLoading();
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

        VolleySingleton.getInstance(ComentariosActivity.this).addToRequestQueue(cacheRequest);
    }

    private void updateGlobalComentarioList() {
        GlobalData.comentarioDatas.clear();

        if(pComentarioDataList != null) {
            for (int i = 0; i < pComentarioDataList.size(); i++) {
                GlobalData.comentarioDatas.add(pComentarioDataList.get(i));
            }
        }
    }

    private void updateDisplay() {
        if (swipeRefreshLayout.isRefreshing()) {
            pComentarioDataSet.clear();
            adapter.notifyDataSetChanged();

            for (PComentarioData cd : pComentarioDataList) {
                pComentarioDataSet.add(cd);
            }
        } else {
            pComentarioDataSet.clear();
            adapter.notifyDataSetChanged();
            for (PComentarioData cd : pComentarioDataList) {
                pComentarioDataSet.add(cd);
            }
        }
        stopLoading();

        if(pComentarioDataSet != null) {
            adapter.notifyItemInserted(pComentarioDataSet.size());
        }
    }

    public void initData(){
        try {
            nombrerestaurante = findViewById(R.id.NombreRestaurante);
            nombrerestaurante.setText(restaurante_nombre);

            jsonStatusSuccessString = getResources().getString(R.string.json_status_success);
            final String URL = Config.APP_API_URL + Config.GET_FOTOS_SLIDER;
            requestData(Config.APP_API_URL + Config.GET_COMENTARIOS+restaurante_id);
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


                                        TextSliderView textSliderView = new TextSliderView(ComentariosActivity.this);
                                        textSliderView
                                                .description(textoslider)
                                                .image(HashMapForURL.get(textoslider))
                                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                                .setOnSliderClickListener(ComentariosActivity.this);
                                        textSliderView.bundle(new Bundle());
                                        textSliderView.getBundle()
                                                .putString("extra", textoslider);
                                        mDemoSlider.addSlider(textSliderView);
                                    }

                                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

                                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

                                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());

                                    mDemoSlider.setDuration(3000);

                                    mDemoSlider.addOnPageChangeListener(ComentariosActivity.this);
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

        ConnectivityManager cm = (ConnectivityManager) ComentariosActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                new AlertDialog.Builder(ComentariosActivity.this, R.style.AppCompatAlertDialogStyle);
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
        Intent intent = new Intent(ComentariosActivity.this,MapaActivity.class);
        ComentariosActivity.this.startActivity(intent);
    }
}

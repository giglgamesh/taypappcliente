package pe.oranch.taypappcliente.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import pe.oranch.taypappcliente.adapter.Tay_comidaAdapter;
import pe.oranch.taypappcliente.entidades.Tay_comida;
import pe.oranch.taypappcliente.listeners.ClickListener;
import pe.oranch.taypappcliente.listeners.RecyclerTouchListener;
import pe.oranch.taypappcliente.models.PComidaData;
import pe.oranch.taypappcliente.utilities.Utils;
import pe.oranch.taypappcliente.utilities.CacheRequest;
import pe.oranch.taypappcliente.utilities.VolleySingleton;

public class DescubrirActivity extends AppCompatActivity {
    ArrayList<Tay_comida> listaComida;
    RecyclerView idrecyclerlista;
    //PARA EL REFRESH LAYOUT
    SwipeRefreshLayout swipeRefreshLayout;
    //FIN REFRESH LAYOUT
    //Nuevas Variables
    private String jsonStatusSuccessString;
    private String connectionError;
    private ArrayList<PComidaData> pComidaDataList;
    private ArrayList<PComidaData> pComidaDataSet;
    private Tay_comidaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descubir);
        initUI();
        initData();
    }
    @Override
    public void onDestroy() {

        try {
            idrecyclerlista = null;
            swipeRefreshLayout = null;
            //p.shutdown();
            GlobalData.comidadata = null;
            super.onDestroy();
        }catch (Exception e){
            super.onDestroy();
        }
    }

    public void IniciarObjetos(){
        //INICIALIZAR REFRESH LAYOUT
        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        //FIN REFRESH LAYOUT
        //REFRESH
        //swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        //    @Override
        //    public void onRefresh() {
                // cancel the Visual indication of a refresh
        //        swipeRefreshLayout.setRefreshing(false);
        //        actualizarComidas();
        //    }
        //});


        idrecyclerlista = findViewById(R.id.idRecyclerLista);
        idrecyclerlista.setLayoutManager(new LinearLayoutManager(DescubrirActivity.this));
        idrecyclerlista.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(DescubrirActivity.this);
        idrecyclerlista.setLayoutManager(llm);

        pComidaDataSet = new ArrayList<>();
        adapter = new Tay_comidaAdapter(DescubrirActivity.this, pComidaDataSet);
        idrecyclerlista.setAdapter(adapter);

        idrecyclerlista.addOnItemTouchListener(new RecyclerTouchListener(DescubrirActivity.this, idrecyclerlista, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                onItemClicked(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    private void initUI (){
        IniciarObjetos();
        initSwipeRefreshLayout();
    }
    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(Config.APP_API_URL + Config.GET_ALL_TIPOS_COMIDA);
            }
        });
    }


    public void VolverPerfil(View view) {
        onBackPressed();
    }

    public void actualizarComidas(){
        initData();
    }

    private void initData(){
        try {
            requestData(Config.APP_API_URL + Config.GET_ALL_TIPOS_COMIDA);
            jsonStatusSuccessString = getResources().getString(R.string.json_status_success);
            connectionError = getResources().getString(R.string.connection_error);
            //final String URL = Config.APP_API_URL + Config.GET_TIPOS_COMIDA;
            //Utils.psLog(URL);
            //getTipoComidas(URL);
        }catch (Exception e){
            Utils.psErrorLogE("Error in bind Rating", e);
        }
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
                        Type listType = new TypeToken<List<PComidaData>>() {
                        }.getType();

                        pComidaDataList = gson.fromJson(jsonObject.getString("data"), listType);


                        if(pComidaDataList != null && pComidaDataList.size() > 1) {
                            idrecyclerlista.setVisibility(View.VISIBLE);
                            updateDisplay();
                        }
                        updateGlobalCityList();
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

        VolleySingleton.getInstance(DescubrirActivity.this).addToRequestQueue(cacheRequest);
    }

    private void onItemClicked(int position) {
        Utils.psLog("Position : " + position);
        Intent intent;
        intent = new Intent(DescubrirActivity.this,DescubrirComidaActivity.class);
        GlobalData.comidadata = pComidaDataList.get(position);
        intent.putExtra("tay_tipocomida_id", pComidaDataList.get(position).tay_tipocomida_id);
        intent.putExtra("tay_tipocomida_nombre", pComidaDataList.get(position).tay_tipocomida_nombre);
        DescubrirActivity.this.startActivity(intent);
        DescubrirActivity.this.overridePendingTransition(R.anim.right_to_left, R.anim.blank_anim);
    }

    private void updateGlobalCityList() {
        GlobalData.comidaDatas.clear();

        if(pComidaDataList != null) {
            for (int i = 0; i < pComidaDataList.size(); i++) {
                GlobalData.comidaDatas.add(pComidaDataList.get(i));
            }
        }
    }

    private void updateDisplay() {
        if (swipeRefreshLayout.isRefreshing()) {
            pComidaDataSet.clear();
            adapter.notifyDataSetChanged();

            for (PComidaData cd : pComidaDataList) {
                pComidaDataSet.add(cd);
            }
        } else {
            pComidaDataSet.clear();
            adapter.notifyDataSetChanged();
            for (PComidaData cd : pComidaDataList) {
                pComidaDataSet.add(cd);
            }
        }
        stopLoading();

        if(pComidaDataSet != null) {
            adapter.notifyItemInserted(pComidaDataSet.size());
        }
    }

    private void stopLoading(){
        try {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }catch (Exception e){}
    }

    private void getTipoComidas(String postURL) {
        if (isInternetOn()) {
            JsonObjectRequest req = new JsonObjectRequest(postURL,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                listaComida = new ArrayList<>();
                                idrecyclerlista = findViewById(R.id.idRecyclerLista);
                                idrecyclerlista.setLayoutManager(new LinearLayoutManager(DescubrirActivity.this));
                                idrecyclerlista.setHasFixedSize(true);

                                Tay_comida tay_comida = null;
                                String success_status = response.getString("status");
                                if (success_status.equals(jsonStatusSuccessString)) {
                                    JSONArray datjson = response.optJSONArray("data");
                                    for (int i = 0; i < datjson.length(); i++) {
                                        tay_comida = new Tay_comida();
                                        JSONObject jsonObject = null;
                                        jsonObject = datjson.getJSONObject(i);
                                        tay_comida.setTay_tipocomida_nombre(jsonObject.optString("tay_tipocomida_nombre"));
                                        tay_comida.setTay_tipocomida_id(Integer.parseInt(jsonObject.optString("tay_tipocomida_id")));
                                        tay_comida.setTay_tipocomida_url(jsonObject.optString("tay_tipocomida_url"));
                                        listaComida.add(tay_comida);
                                    }
                                    //DESCOMENTAR
                                    //Tay_comidaAdapter adapter=new Tay_comidaAdapter(DescubrirActivity.this,listaComida);
                                    //idrecyclerlista.setAdapter(adapter);
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
    public final boolean isInternetOn() {

        ConnectivityManager cm = (ConnectivityManager) DescubrirActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                new AlertDialog.Builder(DescubrirActivity.this, R.style.AppCompatAlertDialogStyle);
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
        Intent intent = new Intent(DescubrirActivity.this,MapaActivity.class);
        DescubrirActivity.this.startActivity(intent);
    }
}

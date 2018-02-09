package pe.oranch.taypappcliente.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pe.oranch.taypappcliente.Config;
import pe.oranch.taypappcliente.GlobalData;
import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.adapter.Tay_cartaAdapter;
import pe.oranch.taypappcliente.models.PCartaData;
import pe.oranch.taypappcliente.utilities.CacheRequest;
import pe.oranch.taypappcliente.utilities.Utils;
import pe.oranch.taypappcliente.utilities.VolleySingleton;

public class CartaActivity extends AppCompatActivity {
    RecyclerView idrecyclerlista;
    //PARA EL REFRESH LAYOUT
    SwipeRefreshLayout swipeRefreshLayout;
    //FIN REFRESH LAYOUT
    //Nuevas Variables
    private String jsonStatusSuccessString;
    private String connectionError;
    private String id_carta_empresa;
    private ArrayList<PCartaData> pCartaDataList;
    private ArrayList<PCartaData> pCartaDataSet;
    private Tay_cartaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta);
        initUI();
        initData();
    }
    @Override
    public void onDestroy() {

        try {
            idrecyclerlista = null;
            swipeRefreshLayout = null;
            //p.shutdown();
            GlobalData.cartadata = null;
            super.onDestroy();
        }catch (Exception e){
            super.onDestroy();
        }
    }

    public void IniciarObjetos(){
        idrecyclerlista = findViewById(R.id.idRecyclerLista);
        idrecyclerlista.setLayoutManager(new LinearLayoutManager(CartaActivity.this));
        idrecyclerlista.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(CartaActivity.this);
        idrecyclerlista.setLayoutManager(llm);

        pCartaDataSet = new ArrayList<>();
        adapter = new Tay_cartaAdapter(CartaActivity.this, pCartaDataSet);
        idrecyclerlista.setAdapter(adapter);
    }
    private void initUI (){
        id_carta_empresa = getIntent().getStringExtra("restaurante_id");
        IniciarObjetos();
        initSwipeRefreshLayout();
    }
    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(Config.APP_API_URL + Config.GET_CARTA+id_carta_empresa);
            }
        });
    }


    public void VolverPerfil(View view) {
        onBackPressed();
    }

    private void initData(){
        try {

            requestData(Config.APP_API_URL + Config.GET_CARTA+id_carta_empresa);
            jsonStatusSuccessString = getResources().getString(R.string.json_status_success);
            connectionError = getResources().getString(R.string.connection_error);
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
                        Type listType = new TypeToken<List<PCartaData>>() {
                        }.getType();

                        pCartaDataList = gson.fromJson(jsonObject.getString("data"), listType);


                        if(pCartaDataList != null && pCartaDataList.size() > 1) {
                            idrecyclerlista.setVisibility(View.VISIBLE);
                            updateDisplay();
                        }
                        updateGlobalCartaList();
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

        VolleySingleton.getInstance(CartaActivity.this).addToRequestQueue(cacheRequest);
    }

    private void updateGlobalCartaList() {
        GlobalData.cartaDatas.clear();

        if(pCartaDataList != null) {
            for (int i = 0; i < pCartaDataList.size(); i++) {
                GlobalData.cartaDatas.add(pCartaDataList.get(i));
            }
        }
    }

    private void updateDisplay() {
        if (swipeRefreshLayout.isRefreshing()) {
            pCartaDataSet.clear();
            adapter.notifyDataSetChanged();

            for (PCartaData cd : pCartaDataList) {
                pCartaDataSet.add(cd);
            }
        } else {
            pCartaDataSet.clear();
            adapter.notifyDataSetChanged();
            for (PCartaData cd : pCartaDataList) {
                pCartaDataSet.add(cd);
            }
        }
        stopLoading();

        if(pCartaDataSet != null) {
            adapter.notifyItemInserted(pCartaDataSet.size());
        }
    }

    private void stopLoading(){
        try {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }catch (Exception e){}
    }

    public void VerMapa(View view) {
        Intent intent = new Intent(CartaActivity.this,MapaActivity.class);
        CartaActivity.this.startActivity(intent);
    }
}

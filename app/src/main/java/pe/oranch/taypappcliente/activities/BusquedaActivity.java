package pe.oranch.taypappcliente.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class BusquedaActivity extends AppCompatActivity {
    TextView cerrarbusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        initUI();
    }
    @Override
    public void onDestroy() {

        try {
            super.onDestroy();
        }catch (Exception e){
            super.onDestroy();
        }
    }

    public void IniciarObjetos(){
        cerrarbusqueda = (TextView) findViewById(R.id.CerrarBusqueda);
        cerrarbusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolverPerfil (view);
            }
        });
    }
    private void initUI (){
        IniciarObjetos();
    }

    public void VolverPerfil(View view) {
        onBackPressed();
    }
}

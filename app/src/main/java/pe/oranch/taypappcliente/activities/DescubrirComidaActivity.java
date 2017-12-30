package pe.oranch.taypappcliente.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.adapter.Tay_comidaAdapter;
import pe.oranch.taypappcliente.adapter.Tay_restaurantesAdapter;
import pe.oranch.taypappcliente.entidades.Tay_comida;
import pe.oranch.taypappcliente.entidades.Tay_restaurantesjoin;
import pe.oranch.taypappcliente.request.ListarComidasRequest;
import pe.oranch.taypappcliente.request.ListarRestaurantesRequest;

public class DescubrirComidaActivity extends AppCompatActivity {
    ArrayList<Tay_restaurantesjoin> listaRestaurantes;
    RecyclerView idrecyclerlista;
    //PARA EL REFRESH LAYOUT
    SwipeRefreshLayout swipeRefreshLayout;
    //FIN REFRESH LAYOUT
    //Variables para la vista
    TextView titulo_comida;
    //fin variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descubir_comida);
        ObtenerRestaurantes();
        IniciarObjetos();
    }

    public void IniciarObjetos(){
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
        titulo_comida = (TextView) findViewById(R.id.Titulo_Comida);
        Intent intentdatos = getIntent();
        final String titulo = intentdatos.getStringExtra("tay_tipocomida_nombre");
        titulo_comida.setText(titulo);
    }

    public void VolverPerfil(View view) {
        onBackPressed();
    }

    public void actualizarComidas(){
        //ObtenerRestaurantes();
    }

    public void ObtenerRestaurantes(){
        Intent intentdatos = getIntent();
        final Integer valorTipoComida = Integer.parseInt(intentdatos.getStringExtra("tay_tipocomida_id"));

        Response.Listener<String> responseListenerLista = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    listaRestaurantes = new ArrayList<>();
                    idrecyclerlista = findViewById(R.id.idRecyclerLista);
                    idrecyclerlista.setLayoutManager(new LinearLayoutManager(DescubrirComidaActivity.this));
                    idrecyclerlista.setHasFixedSize(true);

                    JSONObject jsonReponse = new JSONObject(response);
                    Tay_restaurantesjoin tay_restaurantesjoin=null;
                    JSONArray json=jsonReponse.optJSONArray("usuario");
                    for (int i=0;i<json.length();i++){
                        tay_restaurantesjoin=new Tay_restaurantesjoin();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);

                        tay_restaurantesjoin.setTay_empresa_nombre(jsonObject.optString("tay_empresa_nombre"));
                        tay_restaurantesjoin.setTay_tipocomida_nombre(jsonObject.optString("tay_tipocomida_nombre"));
                        tay_restaurantesjoin.setTay_empresa_direccion(jsonObject.optString("tay_empresa_direccion"));
                        //tay_restaurantesjoin.setTay_calificacion_calificacion(Integer.parseInt(jsonObject.optString("tay_calificacion_calificacion")));
                        listaRestaurantes.add(tay_restaurantesjoin);
                    }
                    Tay_restaurantesAdapter adapter=new Tay_restaurantesAdapter(DescubrirComidaActivity.this,listaRestaurantes);
                    idrecyclerlista.setAdapter(adapter);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        ListarRestaurantesRequest listarrestaurantesRequest = new ListarRestaurantesRequest(valorTipoComida,responseListenerLista);
        RequestQueue queue = Volley.newRequestQueue(DescubrirComidaActivity.this);
        queue.add(listarrestaurantesRequest);
    }
}

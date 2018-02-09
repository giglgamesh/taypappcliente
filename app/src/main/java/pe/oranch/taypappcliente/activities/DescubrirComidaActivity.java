package pe.oranch.taypappcliente.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.oranch.taypappcliente.Config;
import pe.oranch.taypappcliente.GlobalData;
import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.adapter.Tay_restaurantesAdapter;
import pe.oranch.taypappcliente.entidades.Tay_restaurantesjoin;
import pe.oranch.taypappcliente.listeners.ClickListener;
import pe.oranch.taypappcliente.listeners.RecyclerTouchListener;
import pe.oranch.taypappcliente.models.PComidaData;
import pe.oranch.taypappcliente.models.PRestauranteData;
import pe.oranch.taypappcliente.models.RestauranteRowData;
import pe.oranch.taypappcliente.request.ListarRestaurantesRequest;
import pe.oranch.taypappcliente.utilities.Utils;

public class DescubrirComidaActivity extends AppCompatActivity {
    ArrayList<Tay_restaurantesjoin> listaRestaurantes;
    RecyclerView idrecyclerlista;
    //PARA EL REFRESH LAYOUT
    SwipeRefreshLayout swipeRefreshLayout;
    //FIN REFRESH LAYOUT
    //Variables para la vista
    TextView titulo_comida;
    //fin variables
    //Nuevas Variables
    private String jsonStatusSuccessString;
    private int valorTipoComida;
    private String nombreTipoComida;
    private PComidaData pComida;
    private int selectedComidaID;
    private StaggeredGridLayoutManager mLayoutManager;
    private List<RestauranteRowData> miDataset = new ArrayList<>();
    private RestauranteRowData info;
    private Tay_restaurantesAdapter mAdapter;
    private CoordinatorLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descubir_comida);
        //IniciarObjetos();
        initUI();
        initData();
        saveSelectedComidaInfo(pComida);
        loadRestauranteGrid();
        //ObtenerRestaurantes();
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.blank_anim, R.anim.left_to_right);
    }

    @Override
    protected void onDestroy() {
        try {
            Utils.psLog("Clearing Objects on Destroy");

            idrecyclerlista.addOnItemTouchListener(null);
            mLayoutManager = null;
            miDataset.clear();
            mAdapter = null;
            miDataset = null;
            //p.shutdown();
            Utils.unbindDrawables(mainLayout);

            super.onDestroy();
        }catch (Exception e){
            super.onDestroy();
        }

    }

    private void initUI() {
        titulo_comida = (TextView) findViewById(R.id.Titulo_Comida);
        nombreTipoComida = getIntent().getStringExtra("tay_tipocomida_nombre");
        titulo_comida.setText(nombreTipoComida);
    }

    private void saveSelectedComidaInfo(PComidaData ct) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("_comidaid", ct.tay_tipocomida_id);
            editor.putString("_comidanombre", ct.tay_tipocomida_nombre);
            editor.putString("_url", ct.tay_tipocomida_url);
            editor.putInt("_estado", ct.tay_tipocomida_estado);
            editor.commit();
        } catch (Exception e) {
            Utils.psErrorLogE("Error en saveSelectedComidaInfo.", e);
        }
    }
    public void loadRestauranteGrid() {
        try {
            idrecyclerlista = (RecyclerView) findViewById(R.id.idRecyclerLista);

            idrecyclerlista.setHasFixedSize(true);

            mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            idrecyclerlista.setLayoutManager(mLayoutManager);

            mAdapter = new Tay_restaurantesAdapter(this, miDataset, idrecyclerlista);
            idrecyclerlista.setAdapter(mAdapter);

            for(PRestauranteData cd : pComida.restaurantes) {
                info = new RestauranteRowData();
                info.setTay_empresa_id(cd.tay_empresa_id);
                info.setTay_empresa_nombre(cd.tay_empresa_nombre);
                info.setTay_empresa_direccion(cd.tay_empresa_direccion);
                info.setTay_empresa_telefono(cd.tay_empresa_telefono);
                info.setTay_empresa_horainicial(cd.tay_empresa_horainicial);
                info.setTay_empresa_horafin(cd.tay_empresa_horafin);
                info.setTay_empresa_latitud(cd.tay_empresa_latitud);
                info.setTay_empresa_longitud(cd.tay_empresa_longitud);
                info.setRestaurante_comida_nombre(cd.restaurante_comida_nombre);
                info.setRestaurante_rating(cd.restaurante_rating);
                info.setPrecio_desde_carta(cd.precio_desde_carta);
                info.setPrecio_desde_menu(cd.precio_desde_menu);
                info.setRestaurante_oferta(cd.restaurante_oferta);
                info.setMenufoto(cd.menufoto);
                miDataset.add(info);
            }

            mAdapter.notifyItemInserted(miDataset.size());

            idrecyclerlista.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        } catch (Resources.NotFoundException e) {
            Utils.psErrorLogE("Error in loadCategoryGrid.", e);
        }
    }

    public void VolverPerfil(View view) {
        onBackPressed();
    }

    private void initData(){
        try {
            pComida = GlobalData.comidadata;
            selectedComidaID = pComida.tay_tipocomida_id;
        }catch (Exception e){
            Utils.psErrorLogE("Error in bind Rating", e);
        }
    }

    public void VerMapa(View view) {
        Intent intent = new Intent(DescubrirComidaActivity.this,MapaActivity.class);
        DescubrirComidaActivity.this.startActivity(intent);
    }
}

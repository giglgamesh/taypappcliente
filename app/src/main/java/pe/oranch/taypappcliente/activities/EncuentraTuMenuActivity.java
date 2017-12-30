package pe.oranch.taypappcliente.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.adapter.Tay_comidaAdapter;
import pe.oranch.taypappcliente.entidades.Tay_comida;
import pe.oranch.taypappcliente.request.ListarComidasRequest;

public class EncuentraTuMenuActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {
    //PARA EL REFRESH LAYOUT
    SwipeRefreshLayout swipeRefreshLayout;
    //FIN REFRESH LAYOUT
    //PARA EL SLIDER
    private SliderLayout mDemoSlider;
    HashMap<String, String> HashMapForURL ;
    HashMap<String, Integer> HashMapForLocalRes ;
    //FIN PARA EL SLIDER

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuentra_tu_menu);
        ObtenerTiposComida();
        IniciarObjetos();
        IniciarSlider();
        //mDemoSlider.stopAutoCycle();
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
    }

    public void IniciarSlider(){
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        HashMapForLocalRes = new HashMap<String, Integer>();

        HashMapForLocalRes.put("Prueba1",R.drawable.criolla_foto);
        HashMapForLocalRes.put("Prueba2",R.drawable.criolla_foto);
        HashMapForLocalRes.put("Prueba3",R.drawable.criolla_foto);
        HashMapForLocalRes.put("Prueba4",R.drawable.criolla_foto);
        HashMapForLocalRes.put("Prueba5",R.drawable.criolla_foto);

        for(String name : HashMapForLocalRes.keySet()){

            TextSliderView textSliderView = new TextSliderView(EncuentraTuMenuActivity.this);

            textSliderView
                    .description(name)
                    .image(HashMapForLocalRes.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());

            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mDemoSlider.setCustomAnimation(new DescriptionAnimation());

        mDemoSlider.setDuration(3000);

        mDemoSlider.addOnPageChangeListener(EncuentraTuMenuActivity.this);
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
        Intent intent = new Intent(EncuentraTuMenuActivity.this,PrincipalActivity.class);
        EncuentraTuMenuActivity.this.startActivity(intent);
    }

    public void actualizarComidas(){

    }

    public void ObtenerTiposComida(){

    }

    public void Descubrir(View view) {
        Intent intent = new Intent(EncuentraTuMenuActivity.this,DescubrirActivity.class);
        EncuentraTuMenuActivity.this.startActivity(intent);
    }
}

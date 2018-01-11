package pe.oranch.taypappcliente.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.View;

import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.utilities.Utils;

public class MapaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SpannableString exploreOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        initData();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.blank_anim, R.anim.left_to_right);
    }


    private void initData() {
        try {
            exploreOnMap = Utils.getSpannableString(getString(R.string.explore_on_map));
        }catch(Exception e){
            Utils.psErrorLogE("Error in init data.", e);
        }
    }


    public void VolverPerfil(View view) {
        onBackPressed();
    }
}

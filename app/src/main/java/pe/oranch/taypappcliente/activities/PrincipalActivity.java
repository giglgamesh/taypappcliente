package pe.oranch.taypappcliente.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.recursos.PrefManager;

public class PrincipalActivity extends AppCompatActivity {
    JSONObject response, profile_pic_data, profile_pic_url;
    //variable del archivo de sesion
    private SharedPreferences pref;
    //VARIABLES RECORDAR SESION
    private PrefManager prefManager;
    //FIN VARIABLES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        pref = PreferenceManager.getDefaultSharedPreferences(PrincipalActivity.this.getBaseContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Revisar si es la primera vez que inicia el prefManager()
        prefManager = new PrefManager(this);
        String loguinRealidad = pref.getString("loguinFacebook","");
        if (loguinRealidad.equals("verdad")){
            perfilusuariofacebook();
        }else{
            perfilusuarionormal();
        }
    }
    private void perfilusuariofacebook(){
        Intent intent = getIntent();
        String jsondata = pref.getString("userProfile","");
        Log.w("Jsondata", jsondata);
        TextView user_name = (TextView) findViewById(R.id.UserName);
        CircleImageView user_picture = (CircleImageView) findViewById(R.id.perfilImagen);
        ImageView imagenmenu = (ImageView) findViewById(R.id.imagenMenu);
        TextView user_email = (TextView) findViewById(R.id.email);
        try {
            response = new JSONObject(jsondata);
            user_email.setText(response.get("email").toString());
            user_name.setText(("HOLA "+response.get("name").toString()).toUpperCase());
            profile_pic_data = new JSONObject(response.get("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            Picasso.with(this).load(profile_pic_url.getString("url"))
                    .into(user_picture);
            Picasso.with(this).load(profile_pic_url.getString("url"))
                    .into(imagenmenu);

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    private void perfilusuarionormal(){
        TextView user_name = (TextView) findViewById(R.id.UserName);
        //CircleImageView user_picture = (CircleImageView) findViewById(R.id.perfilImagen);
        //ImageView imagenmenu = (ImageView) findViewById(R.id.imagenMenu);
        TextView user_email = (TextView) findViewById(R.id.email);
        try {
            user_email.setText(pref.getString("tay_cliente_email",""));
            user_name.setText(("HOLA "+pref.getString("tay_cliente_nombre","")).toUpperCase());
            //Picasso.with(this).load(profile_pic_url.getString("url")).into(imagenmenu);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void Descubir(View view) {
        Intent intent = new Intent(PrincipalActivity.this,DescubrirActivity.class);
        PrincipalActivity.this.startActivity(intent);
    }

    public void Favoritos(View view) {
        Intent intent = new Intent(PrincipalActivity.this,EncuentraTuMenuActivity.class);
        PrincipalActivity.this.startActivity(intent);
    }

    public void Notificaciones(View view) {
    }

    public void CerrarSesion(View view) {
        String loguinRealidad = pref.getString("loguinFacebook","");
        if (loguinRealidad.equals("verdad")){
            prefManager.setFirstTimeLaunch(true);
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(PrincipalActivity.this,InicioActivity.class);
            PrincipalActivity.this.startActivity(intent);
            finish();
        }else{
            prefManager.setFirstTimeLaunch(true);
            Intent intent = new Intent(PrincipalActivity.this,InicioActivity.class);
            PrincipalActivity.this.startActivity(intent);
            finish();
        }
    }

    public void VolverEncuentra(View view) {
        onBackPressed();
    }
}

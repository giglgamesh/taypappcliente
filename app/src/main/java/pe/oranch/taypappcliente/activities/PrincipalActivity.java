package pe.oranch.taypappcliente.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.oranch.taypappcliente.R;

public class PrincipalActivity extends AppCompatActivity {
    JSONObject response, profile_pic_data, profile_pic_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        perfilusuario();
    }
    private void perfilusuario(){
        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("userProfile");
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

    public void Descubir(View view) {
        Intent intent = new Intent(PrincipalActivity.this,DescubrirActivity.class);
        PrincipalActivity.this.startActivity(intent);
    }

    public void Favoritos(View view) {
    }

    public void Notificaciones(View view) {
    }
}

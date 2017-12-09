package pe.oranch.taypappcliente.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import pe.oranch.taypappcliente.recursos.PrefManager;
import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.request.LoguinRequest;
import pe.oranch.taypappcliente.utilities.Utils;

public class InicioActivity extends AppCompatActivity {
    private ProgressDialog prgDialog;
    private SpannableString loginString;
    //VARIABLE PARA REGISTRAR
    TextView btn_registrarse;
    //FIN VARIABLE
    //VARIABLES GENERALES
    EditText tv_usuario,tv_password;
    //FIN VARIABLES GENERALES

    //FUNCION PARA RETORNAR Y NO SALIR
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Realmente desea salir?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                            System.exit(0);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
        return true;
    }
    //FIN DE LA FUNCION

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //BOTON REGISTRARSE
       // btn_registrarse = (TextView) findViewById(R.id.btn_Registrarse);
        //btn_registrarse.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {

                //iniciar actividad
         //       Intent intent = new Intent(InicioActivity.this,RegistrarUsuarioActivity.class);
         //       InicioActivity.this.startActivity(intent);
                //fin inicio de la actividad
         //   }
        //});
        //FIN FUNCION BOTON

    }

    public void registrarvista(View view) {
        //iniciar actividad
        Intent intent = new Intent(InicioActivity.this,RegistrarUsuarioActivity.class);
        InicioActivity.this.startActivity(intent);
        //fin inicio de la actividad
    }

    public void IniciarLogueo(View view) {
        //SETEO DE CAMPOS PARA LOGUIN
        tv_usuario = (EditText) findViewById(R.id.Tv_usuario);
        tv_password = (EditText) findViewById(R.id.Tv_password);
        //FIN DE SETEO
        final String username = tv_usuario.getText().toString();
        final String password = tv_password.getText().toString();
        if (inputValidation()){
            if(isInternetOn()) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonReponse = new JSONObject(response);
                            boolean success= jsonReponse.getBoolean("success");

                            if (success){
                                //retorna el valor de nombre de la base de datos en el jason
                                String idcliente = jsonReponse.getString("tay_cliente_id");
                                String nombre = jsonReponse.getString("tay_cliente_nombre");
                                String usuario = jsonReponse.getString("tay_cliente_email");
                                String tipo = jsonReponse.getString("tay_cliente_tipo");
                                String fecha = jsonReponse.getString("tay_cliente_fecha");
                                String estado = jsonReponse.getString("tay_cliente_estado");
                                //fin retorno

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(InicioActivity.this.getApplicationContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("tay_cliente_id", Integer.parseInt(idcliente));
                                editor.putString("tay_cliente_nombre", nombre);
                                editor.putString("tay_cliente_email", usuario);
                                editor.putInt("tay_cliente_tipo", Integer.parseInt(tipo));
                                editor.putString("tay_cliente_fecha", fecha);
                                editor.putInt("tay_cliente_estado", Integer.parseInt(estado));
                                editor.apply();

                                //iniciar actividad
                                Intent intent = new Intent(InicioActivity.this,PrincipalActivity.class);
                                //finalizar actividad
                                //enviar valor
                                intent.putExtra("tay_cliente_nombre", nombre);
                                //fin envio de valor

                                InicioActivity.this.startActivity(intent);
                            }else {
                                AlertDialog.Builder builder= new AlertDialog.Builder(InicioActivity.this);
                                builder.setMessage("Error al Loguear")
                                        .setNegativeButton("Retry",null)
                                        .create().show();
                            }
                        } catch (JSONException e) {
                            prgDialog.cancel();
                            Utils.psLog("Login Fail : " + e.getMessage());
                            e.printStackTrace();
                            showFailPopup();
                            e.printStackTrace();
                        }

                    }
                };

                LoguinRequest loguinRequest = new LoguinRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(InicioActivity.this);
                queue.add(loguinRequest);
            } else {

                showOffline();;

            }
        }
    }
    private void showFailPopup() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(InicioActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.login);
        builder.setMessage(R.string.login_fail);
        builder.setPositiveButton(R.string.OK, null);
        builder.show();
    }
    private void showOffline() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(InicioActivity.this, R.style.AppCompatAlertDialogStyle);
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
    public final boolean isInternetOn() {

        ConnectivityManager cm = (ConnectivityManager) InicioActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
    private boolean inputValidation() {

        if(tv_usuario.getText().toString().equals("")) {
            Toast.makeText(InicioActivity.this.getApplicationContext(), R.string.usuario_mensaje_validacion,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if(tv_password.getText().toString().equals("")) {
            Toast.makeText(InicioActivity.this.getApplicationContext(), R.string.password_mensaje_validacion,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }
    private void initData() {
        try {
            loginString = Utils.getSpannableString(getString(R.string.login));
        }catch(Exception e){
            Utils.psErrorLogE("Error in init data.", e);
        }
    }
}

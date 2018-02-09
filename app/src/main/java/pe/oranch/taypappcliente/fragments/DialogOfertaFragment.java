package pe.oranch.taypappcliente.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import pe.oranch.taypappcliente.Config;
import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.activities.EncuentraTuMenuActivity;
import pe.oranch.taypappcliente.entidades.Tay_slider;
import pe.oranch.taypappcliente.utilities.Utils;
import pe.oranch.taypappcliente.utilities.VolleySingleton;

import static pe.oranch.taypappcliente.Config.APP_IMAGES_LOCAL;

/**
 * Created by Daniel on 27/01/2018.
 */

public class DialogOfertaFragment extends DialogFragment {
    TextView cerrardialog;
    TextView detalledescuento, contenidodialog;
    //Nuevas Variables
    private String jsonStatusSuccessString;

    public static DialogOfertaFragment newInstance() {
        DialogOfertaFragment frag = new DialogOfertaFragment();

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_restaurante_oferta, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        // Set ur code
        initUI(view);
        initData(view);
        return builder.create();
    }

    private void initUI(View view) {
        cerrardialog = (TextView) view.findViewById(R.id.CerrarDialog);
        cerrardialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initData(View view){
        try {
            String id_empresa= getArguments().getString("id_empresa");
            jsonStatusSuccessString = getResources().getString(R.string.json_status_success);
            final String URL = Config.APP_API_URL + Config.GET_OFERTAS + id_empresa;
            Utils.psLog(URL);
            getOferta(URL,view);
        }catch (Exception e){
            Utils.psErrorLogE("Error in bind Rating", e);
        }
    }

    private void getOferta(String postURL, View view) {
        detalledescuento = (TextView) view.findViewById(R.id.DetalleDescuento);
        contenidodialog = (TextView) view.findViewById(R.id.ContenidoDialog);
        if (isInternetOn()) {
            JsonObjectRequest req = new JsonObjectRequest(postURL,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String success_status = response.getString("status");
                                if (success_status.equals(jsonStatusSuccessString)) {
                                    JSONArray datjson = response.optJSONArray("data");
                                    for (int i = 0; i < datjson.length(); i++) {
                                        JSONObject jsonObject = null;
                                        jsonObject = datjson.getJSONObject(i);
                                        detalledescuento.setText(jsonObject.optString("tay_descuento_descuento")+"% de descuento");
                                        contenidodialog.setText(jsonObject.optString("tay_descuento_descripcion"));
                                    }
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
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(req);

        } else {
            showOffline();
        }
    }
    public final boolean isInternetOn() {

        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(getActivity().getApplicationContext(), R.style.AppCompatAlertDialogStyle);
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
}

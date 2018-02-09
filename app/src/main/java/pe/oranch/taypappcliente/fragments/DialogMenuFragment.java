package pe.oranch.taypappcliente.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pe.oranch.taypappcliente.Config;
import pe.oranch.taypappcliente.GlobalData;
import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.adapter.Tay_entradaAdapter;
import pe.oranch.taypappcliente.adapter.Tay_segundoAdapter;
import pe.oranch.taypappcliente.models.PMenuData;
import pe.oranch.taypappcliente.models.PMenuFotoData;
import pe.oranch.taypappcliente.models.PMenuFotoFotoData;
import pe.oranch.taypappcliente.utilities.CacheRequest;
import pe.oranch.taypappcliente.utilities.Utils;
import pe.oranch.taypappcliente.utilities.VolleySingleton;

import static pe.oranch.taypappcliente.Config.APP_API_CARTA;

/**
 * Created by Daniel on 27/01/2018.
 */

public class DialogMenuFragment extends DialogFragment {
    TextView cerrardialog;
    //Nuevas Variables
    private String jsonStatusSuccessString;
    private ArrayList<PMenuData> pMenuDataList, pMenuSegundoDataList;
    private ArrayList<PMenuData> pMenuDataSet, pMenuSegundoDataSet;
    private Tay_entradaAdapter adapterentrada;
    private Tay_segundoAdapter adaptersegundo;
    RecyclerView idrecyclerlista, idrecyclerlistasegundo;

    public static DialogMenuFragment newInstance() {
        DialogMenuFragment frag = new DialogMenuFragment();

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_menu, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        // Set ur code
        initUI(view);
        initData(view);
        return builder.create();
    }

    @Override
    public void onDestroy() {

        try {
            idrecyclerlista = null;
            idrecyclerlistasegundo = null;
            GlobalData.menudata = null;
            GlobalData.menusegundodata = null;
            super.onDestroy();
        }catch (Exception e){
            super.onDestroy();
        }
    }

    private void initUI(View view) {
        IniciarObjetos(view);
    }
    public void IniciarObjetos(View view){
        cerrardialog = (TextView) view.findViewById(R.id.CerrarDialog);
        cerrardialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        idrecyclerlista = view.findViewById(R.id.idRecyclerLista);
        idrecyclerlista.setLayoutManager(new LinearLayoutManager((getActivity().getApplicationContext())));
        idrecyclerlista.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager((getActivity().getApplicationContext()));
        idrecyclerlista.setLayoutManager(llm);

        pMenuDataSet = new ArrayList<>();
        adapterentrada = new Tay_entradaAdapter((getActivity().getApplicationContext()), pMenuDataSet);
        idrecyclerlista.setAdapter(adapterentrada);


        idrecyclerlistasegundo = view.findViewById(R.id.idRecyclerListaSegundo);
        idrecyclerlistasegundo.setLayoutManager(new LinearLayoutManager((getActivity().getApplicationContext())));
        idrecyclerlistasegundo.setHasFixedSize(true);
        LinearLayoutManager l2m = new LinearLayoutManager((getActivity().getApplicationContext()));
        idrecyclerlistasegundo.setLayoutManager(l2m);

        pMenuSegundoDataSet = new ArrayList<>();
        adaptersegundo = new Tay_segundoAdapter((getActivity().getApplicationContext()), pMenuSegundoDataSet);
        idrecyclerlistasegundo.setAdapter(adaptersegundo);
    }
    private void initData(View view){
        try {
            String id_empresa= getArguments().getString("id_empresa");
            requestData((Config.APP_API_URL + Config.GET_MENU_ENTRADA + id_empresa), view);
            requestsegundoData((Config.APP_API_URL + Config.GET_MENU_SEGUNDO + id_empresa), view);
            jsonStatusSuccessString = getResources().getString(R.string.json_status_success);
        }catch (Exception e){
            Utils.psErrorLogE("Error in bind Rating", e);
        }
    }

    private void requestData(String uri, final View view) {
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
                        Type listType = new TypeToken<List<PMenuData>>() {
                        }.getType();

                        pMenuDataList = gson.fromJson(jsonObject.getString("data"), listType);


                        if(pMenuDataList != null && pMenuDataList.size() > 0) {
                            idrecyclerlista.setVisibility(View.VISIBLE);
                            updateDisplay();
                        }
                        updateGlobalMenuFotoList();
                    } else {
                        Utils.psLog("Error in loading MenuFoto.");
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
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

        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(cacheRequest);
    }

    private void requestsegundoData(String uri, final View view) {
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
                        Type listType = new TypeToken<List<PMenuData>>() {
                        }.getType();

                        pMenuSegundoDataList = gson.fromJson(jsonObject.getString("data"), listType);


                        if(pMenuSegundoDataList != null && pMenuSegundoDataList.size() > 0) {
                            idrecyclerlistasegundo.setVisibility(View.VISIBLE);
                            updatesegundoDisplay();
                        }
                        updateGlobalMenuSegundoList();
                    } else {
                        Utils.psLog("Error in loading MenuFoto.");
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
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

        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(cacheRequest);
    }

    private void updateDisplay() {
        for (PMenuData cd : pMenuDataList) {
            pMenuDataSet.add(cd);
        }

        if(pMenuDataSet != null) {
            adapterentrada.notifyItemInserted(pMenuDataSet.size());
        }
    }

    private void updatesegundoDisplay() {
        for (PMenuData cd : pMenuSegundoDataList) {
            pMenuSegundoDataSet.add(cd);
        }

        if(pMenuSegundoDataSet != null) {
            adaptersegundo.notifyItemInserted(pMenuSegundoDataSet.size());
        }
    }

    private void updateGlobalMenuFotoList() {
        GlobalData.menuDatas.clear();

        if(pMenuDataList != null) {
            for (int i = 0; i < pMenuDataList.size(); i++) {
                GlobalData.menuDatas.add(pMenuDataList.get(i));
            }
        }
    }

    private void updateGlobalMenuSegundoList() {
        GlobalData.menusegundoDatas.clear();

        if(pMenuSegundoDataList != null) {
            for (int i = 0; i < pMenuSegundoDataList.size(); i++) {
                GlobalData.menusegundoDatas.add(pMenuSegundoDataList.get(i));
            }
        }
    }
}

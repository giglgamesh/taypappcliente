package pe.oranch.taypappcliente.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.taypappcliente.Config;


/**
 * Created by Daniel on 15/11/2017.
 */

public class ListarRestaurantesRequest extends StringRequest {
    private static final String LISTA_RESTAURANTES_REQUEST_URL= Config.APP_API_URL + Config.LISTAR_RESTAURANTES_SCONEXION;
    private Map<String,String> params;
    public ListarRestaurantesRequest(int valorTipoComida, Response.Listener<String> listener){
        super(Method.POST, LISTA_RESTAURANTES_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_tipocomida_id",valorTipoComida+"");
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }
}

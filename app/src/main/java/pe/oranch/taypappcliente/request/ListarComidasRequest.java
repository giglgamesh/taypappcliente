package pe.oranch.taypappcliente.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.taypappcliente.Config;


/**
 * Created by Daniel on 15/11/2017.
 */

public class ListarComidasRequest extends StringRequest {
    private static final String LISTA_COMIDAS_REQUEST_URL= Config.APP_API_URL + Config.LISTAR_COMIDA;
    private Map<String,String> params;
    public ListarComidasRequest(int empresa, Response.Listener<String> listener){
        super(Method.POST, LISTA_COMIDAS_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_tipocomida_estado",empresa+"");
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }
}

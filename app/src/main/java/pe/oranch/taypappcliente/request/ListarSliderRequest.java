package pe.oranch.taypappcliente.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.taypappcliente.Config;


/**
 * Created by Daniel on 15/11/2017.
 */

public class ListarSliderRequest extends StringRequest {
    private static final String LISTA_SLIDER_REQUEST_URL= Config.APP_API_URL + Config.LISTAR_SLIDER;
    private Map<String,String> params;
    public ListarSliderRequest(int empresa, Response.Listener<String> listener){
        super(Method.POST, LISTA_SLIDER_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_slider_estado",empresa+"");
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }
}

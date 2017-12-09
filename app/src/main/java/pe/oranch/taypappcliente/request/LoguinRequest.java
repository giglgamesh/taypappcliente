package pe.oranch.taypappcliente.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pe.oranch.taypappcliente.Config;

/**
 * Created by Daniel on 02/11/2017.
 */

public class LoguinRequest extends StringRequest{
    private static final String LOGIN_REQUEST_URL=Config.APP_API_URL + Config.POST_USER_LOGIN;
    private Map<String,String> params;
    public LoguinRequest(String usuario, String password, Response.Listener<String> listener){
        super(Method.POST, LOGIN_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put ("tay_cliente_email",usuario);
        params.put ("tay_cliente_contrasena",password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

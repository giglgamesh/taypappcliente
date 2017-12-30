package pe.oranch.taypappcliente;

import android.app.Application;

/**
 * Created by Daniel on 05/11/2017.
 */

public class Config extends Application{
    public static final String APP_API_URL = "http://tayapp.azurewebsites.net/restaurantes/cliente/";
    //public static final String APP_API_URL = "http://192.168.0.25/restaurantes/cliente/";
    public static final String POST_USER_LOGIN = "Login.php";
    public static final String LISTAR_COMIDA = "ListarComidas.php";
    public static final String LISTAR_RESTAURANTES_CONEXION = "ConConexion/TipoComidaPrincipal.php";
    public static final String LISTAR_RESTAURANTES_SCONEXION = "SinConexion/TipoComidaPrincipal.php";
}

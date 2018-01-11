package pe.oranch.taypappcliente;

import android.app.Application;

/**
 * Created by Daniel on 05/11/2017.
 */

public class Config extends Application{
    //public static final String APP_API_URL = "http://tayapp.azurewebsites.net/restaurantes/cliente/";

    public static final String APP_API_URL2 = "https://zigma.azurewebsites.net/index.php";
    public static final String SEARCH_BY_GEO = "/rest/items/search_by_geo/miles/";
    public static final String ITEMS_BY_SUB_CATEGORY = "/rest/items/get/city_id/";
    public static final String APP_IMAGES_URL = "https://zigma.azurewebsites.net/uploads/";

    //public static final String APP_API_URL = "http://192.168.0.25/android-taypapp/index.php";
    public static final String APP_API_URL = "http://192.168.0.25/restaurantes/cliente/";
    //public static final String POST_USER_LOGIN = "/rest/clientes/login";
    //public static final String POST_IS_SLIDER = "/rest/sliders/is_slider/estado/";
    //public static final String GET_ALL = "/rest/sliders/get";
    public static final String POST_USER_LOGIN = "Login.php";

    public static final String LISTAR_COMIDA = "ListarComidas.php";
    public static final String LISTAR_SLIDER = "ListarSlider.php";
    public static final String LISTAR_RESTAURANTES_SCONEXION = "TipoComidaPrincipal.php";
}

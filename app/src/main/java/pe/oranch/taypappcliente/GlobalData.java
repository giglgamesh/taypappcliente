package pe.oranch.taypappcliente;

import android.app.Application;

import java.util.ArrayList;

import pe.oranch.taypappcliente.models.PCartaData;
import pe.oranch.taypappcliente.models.PComentarioData;
import pe.oranch.taypappcliente.models.PComidaData;
import pe.oranch.taypappcliente.models.PMenuData;
import pe.oranch.taypappcliente.models.PMenuFotoData;

/**
 * Created by Daniel on 05/11/2017.
 */

public class GlobalData {
    public static PComidaData comidadata =  null;
    public static PMenuFotoData menufotodata =  null;
    public static PComentarioData comentariodata =  null;
    public static PMenuData menudata =  null;
    public static PMenuData menusegundodata =  null;
    public static PCartaData cartadata =  null;
    public static ArrayList<PComidaData>  comidaDatas =  new ArrayList<PComidaData>();
    public static ArrayList<PCartaData>  cartaDatas =  new ArrayList<PCartaData>();
    public static ArrayList<PMenuFotoData>  menufotoDatas =  new ArrayList<PMenuFotoData>();
    public static ArrayList<PComentarioData>  comentarioDatas =  new ArrayList<PComentarioData>();
    public static ArrayList<PMenuData>  menuDatas =  new ArrayList<PMenuData>();
    public static ArrayList<PMenuData>  menusegundoDatas =  new ArrayList<PMenuData>();
}

package pe.oranch.taypappcliente.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Daniel on 15/01/2018.
 */

public class PMenuFotoFotoData implements Parcelable {

    public int tay_menu_dia_id;
    public String tay_menu_dia_nombre;
    public int tay_menu_dia_estado;
    public String tay_menu_dia_fecha;
    public String tay_menu_dia_ruta;


    protected PMenuFotoFotoData(Parcel in) {
        tay_menu_dia_id = in.readInt();
        tay_menu_dia_nombre = in.readString();
        tay_menu_dia_estado = in.readInt();
        tay_menu_dia_fecha = in.readString();
        tay_menu_dia_ruta = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tay_menu_dia_id);
        dest.writeString(tay_menu_dia_nombre);
        dest.writeInt(tay_menu_dia_estado);
        dest.writeString(tay_menu_dia_fecha);
        dest.writeString(tay_menu_dia_ruta);
    }

    @SuppressWarnings("SIN NINGUN USO")
    public static final Creator<PMenuFotoFotoData> CREATOR = new Creator<PMenuFotoFotoData>() {
        @Override
        public PMenuFotoFotoData createFromParcel(Parcel in) {
            return new PMenuFotoFotoData(in);
        }

        @Override
        public PMenuFotoFotoData[] newArray(int size) {
            return new PMenuFotoFotoData[size];
        }
    };

}

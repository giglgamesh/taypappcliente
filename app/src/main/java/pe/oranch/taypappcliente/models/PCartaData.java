package pe.oranch.taypappcliente.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Panacea-Soft on 6/8/15.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class PCartaData implements Parcelable {

    public int tay_carta_id;
    public String tay_carta_nombre;
    public double tay_carta_precio;
    public String tay_carta_ruta_imagen;

    protected PCartaData(Parcel in) {
        tay_carta_id = in.readInt();
        tay_carta_nombre = in.readString();
        tay_carta_precio = in.readDouble();
        tay_carta_ruta_imagen = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tay_carta_id);
        dest.writeString(tay_carta_nombre);
        dest.writeDouble(tay_carta_precio);
        dest.writeString(tay_carta_ruta_imagen);
    }

    @SuppressWarnings("SIN NINGUN USO")
    public static final Creator<PCartaData> CREATOR = new Creator<PCartaData>() {
        @Override
        public PCartaData createFromParcel(Parcel in) {
            return new PCartaData(in);
        }

        @Override
        public PCartaData[] newArray(int size) {
            return new PCartaData[size];
        }
    };
}

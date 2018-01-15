package pe.oranch.taypappcliente.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Daniel on 15/01/2018.
 */

public class PRestauranteData implements Parcelable {

    public int tay_empresa_id;
    public String tay_empresa_nombre;
    public int tay_empresa_tipocomida;
    public String tay_empresa_direccion;
    public String tay_empresa_horainicial;
    public String tay_empresa_horafin;
    public int tay_empresa_estado;
    public String tay_empresa_telefono;
    public String tay_empresa_celular;
    public String tay_empresa_latitud;
    public String tay_empresa_longitud;
    public String tay_empresa_referencia;
    public int tay_empresa_distrito;
    public String tay_ruta_imagen;
    public String tay_empresa_foto;


    protected PRestauranteData(Parcel in) {
        tay_empresa_id = in.readInt();
        tay_empresa_nombre = in.readString();
        tay_empresa_tipocomida = in.readInt();
        tay_empresa_direccion = in.readString();
        tay_empresa_horainicial = in.readString();
        tay_empresa_horafin = in.readString();
        tay_empresa_estado = in.readInt();
        tay_empresa_telefono = in.readString();
        tay_empresa_celular = in.readString();
        tay_empresa_latitud = in.readString();
        tay_empresa_longitud = in.readString();
        tay_empresa_referencia = in.readString();
        tay_empresa_distrito = in.readInt();
        tay_ruta_imagen = in.readString();
        tay_empresa_foto = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tay_empresa_id);
        dest.writeString(tay_empresa_nombre);
        dest.writeInt(tay_empresa_tipocomida);
        dest.writeString(tay_empresa_direccion);
        dest.writeString(tay_empresa_horainicial);
        dest.writeString(tay_empresa_horafin);
        dest.writeInt(tay_empresa_estado);
        dest.writeString(tay_empresa_telefono);
        dest.writeString(tay_empresa_celular);
        dest.writeString(tay_empresa_latitud);
        dest.writeString(tay_empresa_longitud);
        dest.writeString(tay_empresa_referencia);
        dest.writeInt(tay_empresa_distrito);
        dest.writeString(tay_ruta_imagen);
        dest.writeString(tay_empresa_foto);
    }

    @SuppressWarnings("SIN NINGUN USO")
    public static final Creator<PRestauranteData> CREATOR = new Creator<PRestauranteData>() {
        @Override
        public PRestauranteData createFromParcel(Parcel in) {
            return new PRestauranteData(in);
        }

        @Override
        public PRestauranteData[] newArray(int size) {
            return new PRestauranteData[size];
        }
    };

}

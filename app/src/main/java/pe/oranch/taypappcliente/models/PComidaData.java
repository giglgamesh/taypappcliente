package pe.oranch.taypappcliente.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Panacea-Soft on 6/8/15.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class PComidaData implements Parcelable {

    public int tay_tipocomida_id;
    public String tay_tipocomida_nombre;
    public int tay_tipocomida_estado;
    public String tay_tipocomida_url;
    public ArrayList<PRestauranteData> restaurantes;

    protected PComidaData(Parcel in) {
        tay_tipocomida_id = in.readInt();
        tay_tipocomida_nombre = in.readString();
        tay_tipocomida_estado = in.readInt();
        tay_tipocomida_url = in.readString();

        //PARA LA LLAMADA DE RESTAURANTES POR TIPO DE COMIDA//
        if (in.readByte() == 0x01) {
            restaurantes = new ArrayList<PRestauranteData>();
            in.readList(restaurantes, PRestauranteData.class.getClassLoader());
        } else {
            restaurantes = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tay_tipocomida_id);
        dest.writeString(tay_tipocomida_nombre);
        dest.writeInt(tay_tipocomida_estado);
        dest.writeString(tay_tipocomida_url);

        //PARA LA LLAMADA DE RESTAURANTES POR TIPO DE COMIDA//
        if (restaurantes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(restaurantes);
        }
    }

    @SuppressWarnings("SIN NINGUN USO")
    public static final Creator<PComidaData> CREATOR = new Creator<PComidaData>() {
        @Override
        public PComidaData createFromParcel(Parcel in) {
            return new PComidaData(in);
        }

        @Override
        public PComidaData[] newArray(int size) {
            return new PComidaData[size];
        }
    };
}

package pe.oranch.taypappcliente.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Panacea-Soft on 6/8/15.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class PComentarioData implements Parcelable {

    public int tay_comentario_id;
    public String tay_comentario_descripcion;
    public String tay_cliente_nombre;

    protected PComentarioData(Parcel in) {
        tay_comentario_id = in.readInt();
        tay_comentario_descripcion = in.readString();
        tay_cliente_nombre = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tay_comentario_id);
        dest.writeString(tay_comentario_descripcion);
        dest.writeString(tay_cliente_nombre);
    }

    @SuppressWarnings("SIN NINGUN USO")
    public static final Creator<PComentarioData> CREATOR = new Creator<PComentarioData>() {
        @Override
        public PComentarioData createFromParcel(Parcel in) {
            return new PComentarioData(in);
        }

        @Override
        public PComentarioData[] newArray(int size) {
            return new PComentarioData[size];
        }
    };
}

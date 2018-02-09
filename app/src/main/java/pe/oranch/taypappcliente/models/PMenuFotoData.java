package pe.oranch.taypappcliente.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Panacea-Soft on 6/8/15.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class PMenuFotoData implements Parcelable {

    public int tay_menu_dia_id;
    public ArrayList<PMenuFotoFotoData> menufoto;

    protected PMenuFotoData(Parcel in) {
        tay_menu_dia_id = in.readInt();

        //PARA LA LLAMADA DE RESTAURANTES POR TIPO DE COMIDA//
        if (in.readByte() == 0x01) {
            menufoto = new ArrayList<PMenuFotoFotoData>();
            in.readList(menufoto, PMenuFotoFotoData.class.getClassLoader());
        } else {
            menufoto = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tay_menu_dia_id);

        //PARA LA LLAMADA DE RESTAURANTES POR TIPO DE COMIDA//
        if (menufoto == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(menufoto);
        }
    }

    @SuppressWarnings("SIN NINGUN USO")
    public static final Creator<PMenuFotoData> CREATOR = new Creator<PMenuFotoData>() {
        @Override
        public PMenuFotoData createFromParcel(Parcel in) {
            return new PMenuFotoData(in);
        }

        @Override
        public PMenuFotoData[] newArray(int size) {
            return new PMenuFotoData[size];
        }
    };
}

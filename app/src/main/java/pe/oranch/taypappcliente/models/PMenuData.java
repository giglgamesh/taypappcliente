package pe.oranch.taypappcliente.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Panacea-Soft on 6/8/15.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class PMenuData implements Parcelable {

    public int tay_menu_id;
    public String tay_menu_nombre;
    public String tay_menu_fecha;

    protected PMenuData(Parcel in) {
        tay_menu_id = in.readInt();
        tay_menu_nombre = in.readString();
        tay_menu_fecha = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tay_menu_id);
        dest.writeString(tay_menu_nombre);
        dest.writeString(tay_menu_fecha);
    }

    @SuppressWarnings("SIN NINGUN USO")
    public static final Creator<PMenuData> CREATOR = new Creator<PMenuData>() {
        @Override
        public PMenuData createFromParcel(Parcel in) {
            return new PMenuData(in);
        }

        @Override
        public PMenuData[] newArray(int size) {
            return new PMenuData[size];
        }
    };
}

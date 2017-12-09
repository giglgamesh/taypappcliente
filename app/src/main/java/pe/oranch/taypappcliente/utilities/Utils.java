package pe.oranch.taypappcliente.utilities;

import android.text.SpannableString;
import android.util.Log;

/**
 * Created by Daniel on 05/11/2017.
 */

public class Utils {

    private static SpannableString spannableString;
    public static void psLog(String log){
        Log.d("TEAMPS", log);
    }
    public static void psErrorLogE(String log, Exception e) {
        try {
            StackTraceElement l = e.getStackTrace()[0];
            Log.d("TEAMPS", log);
            Log.d("TEAMPS", "Line : " + l.getLineNumber());
            Log.d("TEAMPS", "Method : " + l.getMethodName());
            Log.d("TEAMPS", "Class : " + l.getClassName());
        }catch (Exception ee){}

    }
    public static SpannableString getSpannableString(String str) {
        spannableString = new SpannableString(str);
        return spannableString;
    }
}

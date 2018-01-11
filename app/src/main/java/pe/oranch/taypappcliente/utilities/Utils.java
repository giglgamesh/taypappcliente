package pe.oranch.taypappcliente.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import pe.oranch.taypappcliente.activities.EncuentraTuMenuActivity;

/**
 * Created by Daniel on 05/11/2017.
 */

public class Utils {
    public static EncuentraTuMenuActivity activity;
    private static SpannableString spannableString;

    public Utils(Context context){
        this.activity = (EncuentraTuMenuActivity) context;
    }

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
    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[4].getLineNumber();
    }
    public static String getClassName(Object obj) {
        return ""+ ((Object) obj).getClass();
    }
    public static void psErrorLog(String log, Object obj){
        try {
            Log.d("TEAMPS", log);
            Log.d("TEAMPS", "Line : " + getLineNumber());
            Log.d("TEAMPS", "Class : " + getClassName(obj));
        }catch (Exception ee){}
    }
    public static void unbindDrawables(View view) {
        try{
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }

                if(! (view instanceof AdapterView)) {
                    ((ViewGroup) view).removeAllViews();
                }
            }}catch (Exception e){
            Utils.psErrorLogE("Error in Unbind", e);
        }
    }

    public static boolean isGooglePlayServicesOK(Activity activity) {

        final int GPS_ERRORDIALOG_REQUEST = 9001;

        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, activity, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(activity, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public static int getScreenHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static int getScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }
}

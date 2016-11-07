package com.indglobal.shedoctor.Commans;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anurag on 2/1/16.
 */
public class Comman {

    public static Context mContext;


    public static boolean verifyStoragePermissions(Activity activity,String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && permissions != null) {

            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void setPreferences(Context context, String key, String value) {
        mContext = context;
        SharedPreferences.Editor editor = mContext.getSharedPreferences("WED_APP", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPreferences(Context context, String key) {
        mContext = context;
        SharedPreferences prefs = mContext.getSharedPreferences("WED_APP", Context.MODE_PRIVATE);
        String text = prefs.getString(key, "");
        return text;
    }

    public static void delPrefences(Context context){
        mContext = context;
        SharedPreferences prefs = mContext.getSharedPreferences("WED_APP", Context.MODE_PRIVATE);
        prefs.edit().clear().commit();


    }


    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static boolean passwordValidater(String email) {
        Pattern pattern;
        Matcher matcher;
        final String PASS_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        pattern = Pattern.compile(PASS_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

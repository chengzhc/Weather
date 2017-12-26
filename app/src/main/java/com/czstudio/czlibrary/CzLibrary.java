package com.czstudio.czlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cheng on 2017/12/23.
 */

public class CzLibrary {

    public static void toast(Context context , String info){
        Toast.makeText(context,info,Toast.LENGTH_LONG).show();
    }

    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static void saveUserData(Activity activity,String key, String value){
        SharedPreferences sharedPref = activity.getSharedPreferences("weather_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String loadUserData(Activity activity,String key,String defaultString){
        SharedPreferences sharedPref2 = activity.getSharedPreferences("weather_pref", Context.MODE_PRIVATE);
        String loadedString = sharedPref2.getString(key,defaultString);
        return loadedString;
    }
}

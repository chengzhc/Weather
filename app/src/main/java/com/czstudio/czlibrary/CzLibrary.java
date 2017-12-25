package com.czstudio.czlibrary;

import android.content.Context;
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
}

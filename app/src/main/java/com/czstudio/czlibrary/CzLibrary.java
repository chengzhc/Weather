package com.czstudio.czlibrary;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by cheng on 2017/12/23.
 */

public class CzLibrary {

    public static void toast(Context context , String info){
        Toast.makeText(context,info,Toast.LENGTH_LONG).show();
    }
}

package top.chengzhen1971.weather;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.czstudio.czlibrary.CzLibrary;
import com.czstudio.czlibrary.CzSys_HTTP;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //自身实例
    MainActivity instance;

    Button btn_refresh;
    TextView tv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();

        getWeatherData();

    }

    void initData(){
        instance=this;
    }

    void initView(){

        tv_info = findViewById(R.id.tv_info);
        btn_refresh = findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeatherData();
            }
        });
    }

    void getWeatherData(){
        CzLibrary.toast(this,"开始获取天气");

        Activity activity=this;

        String address = "http://www.sojson.com/open/api/weather/json.shtml";

        HashMap<String,String>  paramsMap = new HashMap<String,String>(){
            {
                put("city","乌鲁木齐");
            }
        };

        CzSys_HTTP.HttpListener listener = new CzSys_HTTP.HttpListener() {
            @Override
            public void onHttpSuccess(String data) {
                //请求成功后的返回
                tv_info.setText(data);
            }

            @Override
            public void onHttpFail(String failInfo) {
                CzLibrary.toast(instance,"请求失败，原因："+failInfo);
            }

            @Override
            public void onHttpNull() {
                CzLibrary.toast(instance,"请求错误，返回空数据");
            }
        };

        CzSys_HTTP.requestGet(activity,address,paramsMap,listener);
    }


}

package top.chengzhen1971.weather;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.czstudio.czlibrary.CzLibrary;
import com.czstudio.czlibrary.CzSys_HTTP;

import org.json.JSONArray;
import org.json.JSONObject;

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
                //tv_info.setText(data);
                processData(data);
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

    /**
     * 1、先取出 kv “data"
     2、从data取出“forcast”
     3、“forcast”取出第一个元素（0）
     * @param weatherStr
     */
    void processData(String weatherStr){
        //1、转成JSONObject
        try {
            JSONObject weatherJson = new JSONObject(weatherStr);
            Log.e("Weather","weatherJson="+weatherJson.toString());

            JSONObject dataJson = weatherJson.getJSONObject("data");
            Log.e("Weather","dataJson="+dataJson.toString());

            JSONArray forecastArray = dataJson.getJSONArray("forecast");
            Log.e("Weather","forecastArray="+forecastArray.toString());

            JSONObject todayJson = forecastArray.getJSONObject(0);
            Log.e("Weather","todayJson="+todayJson.toString());

            String str_weather = "今天天气：\n";
            String date = todayJson.getString("date");
            String sunrise = todayJson.getString("sunrise");
            String high = todayJson.getString("high");
            String low = todayJson.getString("low");
            String sunset = todayJson.getString("sunset");
            String aqi = todayJson.getString("aqi");
            String fx = todayJson.getString("fx");
            String fl = todayJson.getString("fl");
            String type = todayJson.getString("type");
            String notice = todayJson.getString("notice");

            str_weather = str_weather
                    +"日期：" + date +"\n"
                    + type +","+ fx +",风力"+ fl+"\n"
                    +"气温：最"+ high +"，最"+ low +"\n"
                    + "日出时间："+sunrise +"\n"
                    + "日落时间："+sunset +"\n"
                    + "出行提示："+ notice +"\n"
                    + aqi +"\n";

            tv_info.setText(str_weather);


        }catch(Exception e){
            CzLibrary.toast(instance,"JSON数据解析异常："+e);
        }

    }


}

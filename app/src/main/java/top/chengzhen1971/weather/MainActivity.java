package top.chengzhen1971.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.czstudio.czlibrary.CzLibrary;
import com.czstudio.czlibrary.CzSys_HTTP;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //自身实例
    MainActivity instance;

    TextView tv_temp, tv_humidity, tv_pm25, tv_pm10, tv_quality, tv_sick,
            tv_temp_low, tv_temp_hi, tv_sunrise, tv_sunset, tv_wind, tv_notice, tv_date;
    ImageView iv_type;

    EditText et_city;
    Button btn_city,btn_today,btn_future;

    String str_weather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();

        getUserData();
        getWeatherData();

    }

    void initData() {
        instance = this;
    }

    void initView() {
        tv_temp = findViewById(R.id.tv_temp);
        tv_humidity = findViewById(R.id.tv_humidity);
        tv_pm25 = findViewById(R.id.tv_pm25);
        tv_pm10 = findViewById(R.id.tv_pm10);
        tv_quality = findViewById(R.id.tv_quality);
        tv_sick = findViewById(R.id.tv_sick);
        tv_temp_low = findViewById(R.id.tv_temp_low);
        tv_temp_hi = findViewById(R.id.tv_temp_hi);
        tv_sunrise = findViewById(R.id.tv_sunrise);
        tv_sunset = findViewById(R.id.tv_sunset);
        tv_wind = findViewById(R.id.tv_wind);
        tv_notice = findViewById(R.id.tv_notice);
        tv_date = findViewById(R.id.tv_type);

        iv_type = findViewById(R.id.iv_type);

        et_city = findViewById(R.id.et_city);
        et_city.setText("上海");

        btn_city = findViewById(R.id.btn_city);
        btn_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeatherData();
            }
        });

        btn_today = findViewById(R.id.btn_today);
        btn_future = findViewById(R.id.btn_future);
        btn_future.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFutureWeather();
            }
        });
    }

    void getUserData(){
        String city = CzLibrary.loadUserData(this,"city_input","上海");
        et_city.setText(city);
    }

    void getWeatherData() {
        CzLibrary.saveUserData(this,"city_input",et_city.getText().toString());
        CzLibrary.saveUserData(this,"input_time",CzLibrary.getCurrentTime());

        Activity activity = this;

        String address = "http://www.sojson.com/open/api/weather/json.shtml";

        final String city = et_city.getText().toString();

        HashMap<String, String> paramsMap = new HashMap<String, String>() {
            {
                put("city",city );
            }
        };

        CzSys_HTTP.HttpListener listener = new CzSys_HTTP.HttpListener() {
            @Override
            public void onHttpSuccess(String data) {
                //请求成功后的返回
                //tv_info.setText(data);
                str_weather = data;
                processData(data);
            }

            @Override
            public void onHttpFail(String failInfo) {
                CzLibrary.toast(instance, "请求失败，原因：" + failInfo);
            }

            @Override
            public void onHttpNull() {
                CzLibrary.toast(instance, "请求错误，返回空数据");
            }
        };

        CzSys_HTTP.requestGet(activity, address, paramsMap, listener);
    }

    /**
     * 1、先取出 kv “data"
     * 2、从data取出“forcast”
     * 3、“forcast”取出第一个元素（0）
     *
     * @param weatherStr
     */
    void processData(String weatherStr) {
        //1、转成JSONObject
        try {
            JSONObject weatherJson = new JSONObject(weatherStr);
            Log.e("Weather", "weatherJson=" + weatherJson.toString());

            JSONObject dataJson = weatherJson.getJSONObject("data");
            Log.e("Weather", "dataJson=" + dataJson.toString());

            String temp = dataJson.getString("wendu");
            String humidity = dataJson.getString("shidu");
            String pm25 = dataJson.getString("pm25");
            String pm10 = dataJson.getString("pm10");
            String quality = dataJson.getString("quality");
            String sick = dataJson.getString("ganmao");
            tv_temp.setText(temp);
            tv_humidity.setText("湿度：" + humidity);
            tv_pm25.setText("PM2.5：" + pm25);
            tv_pm10.setText("PM10：" + pm10);
            tv_quality.setText("空气质量：" + quality);
            tv_sick.setText("感冒指数：" + sick);


            JSONArray forecastArray = dataJson.getJSONArray("forecast");
            Log.e("Weather", "forecastArray=" + forecastArray.toString());

            JSONObject todayJson = forecastArray.getJSONObject(0);
            Log.e("Weather", "todayJson=" + todayJson.toString());


            String date = CzLibrary.getCurrentTime();
            String sunrise = todayJson.getString("sunrise");
            String high = todayJson.getString("high");
            String low = todayJson.getString("low");
            String sunset = todayJson.getString("sunset");
            String aqi = todayJson.getString("aqi");
            String fx = todayJson.getString("fx");
            String fl = todayJson.getString("fl");
            String type = todayJson.getString("type");
            String notice = todayJson.getString("notice");

            tv_date.setText("日期：" + date);
            tv_temp_low.setText("最" + low);
            tv_temp_hi.setText("最" + high);
            tv_sunrise.setText("日出：" + sunrise);
            tv_sunset.setText("日落：" + sunset);
            tv_wind.setText("风向：" + fx + ",风力：" + fl);
            tv_notice.setText("出行建议：\n" + notice);

            //晴，多云，阴，雨，雪
            if(type.equals("晴")){
                iv_type.setImageResource(R.drawable.clear);
            }else if(type.contains("多云")){
                iv_type.setImageResource(R.drawable.cloud);
            }else if(type.contains("阴")){
                iv_type.setImageResource(R.drawable.overcast);
            }else if(type.contains("雨")){
                iv_type.setImageResource(R.drawable.rain);
            }else if(type.contains("雪")){
                iv_type.setImageResource(R.drawable.snow);
            }else {
                iv_type.setImageResource(R.drawable.temp_high);
            }


        } catch (Exception e) {
            CzLibrary.toast(instance, "JSON数据解析异常：" + e);
        }

    }

    void goToFutureWeather(){
        Intent intent = new Intent(this,FutureActivity.class);
        intent.putExtra("weatherData",str_weather);
        startActivity(intent);
    }


}

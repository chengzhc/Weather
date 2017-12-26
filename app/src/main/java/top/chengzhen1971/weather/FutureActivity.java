package top.chengzhen1971.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

public class FutureActivity extends AppCompatActivity {

    Button btn_today,btn_future;
    ListView lv_future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);

        initData();
        initView();

        showFutureWeather();

    }

    void initData(){

    }

    void initView(){
        btn_today = findViewById(R.id.btn_today);
        btn_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTodayWeather();
            }
        });
        btn_future = findViewById(R.id.btn_future);
        lv_future = findViewById(R.id.lv_future);
    }

    void showFutureWeather(){
        Intent getData = getIntent();
        String weatherData = getData.getStringExtra("weatherData");
        //Log.e("Weather","************************* weather String = "+weatherData);

        try {
            JSONObject weatherJson = new JSONObject(weatherData);
            Log.e("Weather", "weatherJson=" + weatherJson.toString());

            JSONObject dataJson = weatherJson.getJSONObject("data");
            Log.e("Weather", "dataJson=" + dataJson.toString());


            JSONArray forecastArray = dataJson.getJSONArray("forecast");
            Log.e("Weather", "forecastArray=" + forecastArray.toString());

            FutureWeatherAdapter adapter = new FutureWeatherAdapter(this, forecastArray);
            lv_future.setAdapter(adapter);

        } catch (Exception e) {

        }



    }

    void goToTodayWeather(){
        finish();
    }
}

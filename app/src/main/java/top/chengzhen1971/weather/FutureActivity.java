package top.chengzhen1971.weather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FutureActivity extends AppCompatActivity {

    Button btn_today,btn_future;

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
    }

    void showFutureWeather(){
        Intent getData = getIntent();
        String weatherData = getData.getStringExtra("weatherData");
        Log.e("Weather","************************* weather String = "+weatherData);
    }

    void goToTodayWeather(){
        finish();
    }
}

package top.chengzhen1971.weather;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.czstudio.czlibrary.Cz_BaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by cheng on 2017/12/26.
 */

public class FutureWeatherAdapter extends Cz_BaseAdapter {

    //照抄
    public FutureWeatherAdapter(Context parentContext, JSONArray listData) {
        super.init(parentContext, listData);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //这句照抄 , 但要修改R.layout.item_xxxx这个参数
        view = this.mInflater.inflate(R.layout.item_future, null);

        TextView tv_date = view.findViewById(R.id.tv_date);
        TextView tv_type = view.findViewById(R.id.tv_type);
        TextView tv_wind = view.findViewById(R.id.tv_wind);
        TextView tv_temp_lo = view.findViewById(R.id.tv_temp_lo);
        TextView tv_temp_hi = view.findViewById(R.id.tv_temp_hi);
        TextView tv_sunrise = view.findViewById(R.id.tv_sunrise);
        TextView tv_sunset = view.findViewById(R.id.tv_sunset);
        TextView tv_notice = view.findViewById(R.id.tv_notice);
        ImageView iv_type = view.findViewById(R.id.iv_type);

        try {
            JSONObject itemObject = this.dataArray.getJSONObject(i);
            String date = itemObject.getString("date");
            String sunrise = itemObject.getString("sunrise");
            String high = itemObject.getString("high");
            String low = itemObject.getString("low");
            String sunset = itemObject.getString("sunset");
            String aqi = itemObject.getString("aqi");
            String fx = itemObject.getString("fx");
            String fl = itemObject.getString("fl");
            String type = itemObject.getString("type");
            String notice = itemObject.getString("notice");


            switch (i) {
                case 0:
                    tv_date.setText("今天");
                    break;
                case 1:
                    tv_date.setText("明天");
                    break;
                case 2:
                    tv_date.setText("后天");
                    break;
                default:
                    tv_date.setText(date);
                    break;
            }


            tv_temp_lo.setText("最" + low);
            tv_temp_hi.setText("最" + high);
            tv_sunrise.setText("日出：" + sunrise);
            tv_sunset.setText("日落：" + sunset);
            tv_wind.setText("风向：" + fx + ",风力：" + fl);
            tv_type.setText(type);
            tv_notice.setText("出行建议：\n" + notice);

            //晴，多云，阴，雨，雪
            if (type.equals("晴")) {
                iv_type.setImageResource(R.drawable.clear);
            } else if (type.contains("多云")) {
                iv_type.setImageResource(R.drawable.cloud);
            } else if (type.contains("阴")) {
                iv_type.setImageResource(R.drawable.overcast);
            } else if (type.contains("雨")) {
                iv_type.setImageResource(R.drawable.rain);
            } else if (type.contains("雪")) {
                iv_type.setImageResource(R.drawable.snow);
            } else {
                iv_type.setImageResource(R.drawable.temp_high);
            }


        } catch (Exception e) {
            Log.e("FutureWeatherAdapter", "解析JSON异常在第" + i + "个元素" + e);
        }


        return view;
    }

}

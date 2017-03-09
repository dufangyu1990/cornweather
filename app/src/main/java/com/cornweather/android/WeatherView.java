package com.cornweather.android;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cornweather.android.gson.Forecast;
import com.cornweather.android.gson.Weather;
import com.cornweather.android.helper.EventHelper;
import com.cornweather.android.view.ViewImp;

/**
 * Created by dufangyu on 2017/3/8.
 */

public class WeatherView extends ViewImp{

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView titleUpdateTime;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    private ImageView bgImageView;

    private SwipeRefreshLayout swipeRefreshLayout;


    private DrawerLayout drawerLayout;
    private Button navButton;

    @Override
    public int getLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    public void findViews() {
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        degreeText = findViewById(R.id.degree_text);
        titleUpdateTime = findViewById(R.id.title_update_time);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        bgImageView = findViewById(R.id.bgImg);
        swipeRefreshLayout = findViewById(R.id.swip_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = findViewById(R.id.draw_layout);
        navButton = findViewById(R.id.nav_button);
    }


    public void showNdHideWeather(boolean flag)
    {
        if(flag)
        {
            weatherLayout.setVisibility(View.VISIBLE);
        }else{
            weatherLayout.setVisibility(View.INVISIBLE);
        }

    }

    public void setWeatherInfo(Weather weather)
    {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();

        for(Forecast forecast:weather.forecastList)
        {
            View view  = LayoutInflater.from(mRootView.getContext()).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dataText = (TextView)view.findViewById(R.id.date_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxText = (TextView)view.findViewById(R.id.max_text);
            TextView minText = (TextView)view.findViewById(R.id.min_text);
            dataText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

        if(weather.aqi!=null)
        {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度: "+weather.suggestion.comfort.info;
        String carWash = "洗车指数: "+weather.suggestion.carWash.info;
        String sport = "运动建议: "+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }



    public void setBgPic(String pic)
    {
        Glide.with(mRootView.getContext()).load(pic).into(bgImageView);
    }

    @Override
    public void bindEvent() {
        EventHelper.refreshListener(mPresenter,swipeRefreshLayout);
        EventHelper.click(mPresenter,navButton);
    }

    public void closeRefreshOropen(boolean flag)
    {
        if(flag)
        {
            swipeRefreshLayout.setRefreshing(true);
        }else{
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void openDrawlayoutOrclose(String order)
    {
        if(order.equals("close"))
        {
            drawerLayout.closeDrawers();
        }else if(order.equals("open"))
        {
            drawerLayout.openDrawer(GravityCompat.START);
        }

    }
}

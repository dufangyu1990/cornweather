package com.cornweather.android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.cornweather.android.callback.DataCallBack;
import com.cornweather.android.gson.Weather;
import com.cornweather.android.model.ILogicModel;
import com.cornweather.android.model.LogicModelImp;
import com.cornweather.android.present.ActivityPresentImp;
import com.cornweather.android.util.Constant;
import com.cornweather.android.util.Utility;


/**
 * Created by dufangyu on 2017/3/8.
 */

public class WeatherActivity extends ActivityPresentImp<WeatherView> implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {

    private ILogicModel modelBiz;
    private String globalweatherId;
    @Override
    public void afterViewCreate(Bundle savedInstance) {
        super.afterViewCreate(savedInstance);


        if(Build.VERSION.SDK_INT>=21)
        {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        modelBiz = new LogicModelImp();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String bgpic =preferences.getString("bgImg",null);
        if(bgpic!=null)
        {
            mView.setBgPic(bgpic);
        }else{
            loadBgPic();
        }
         String weatherString = preferences.getString("weather",null);
        if(weatherString!=null)
        {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            globalweatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{
            globalweatherId = getIntent().getStringExtra("weather_id");
            mView.showNdHideWeather(false);
            requestWeather(globalweatherId);
        }



    }



    private void requestWeather(final String weatherId)
    {

        String weatherUrl = Constant.WEATHERBASEURL+"?city="+weatherId+"&key="+Constant.KEY;
        modelBiz.getDataFromServer(weatherUrl, "", new DataCallBack() {
            @Override
            public void onSuccess(String response) {
                 Weather weather = Utility.handleWeatherResponse(response);
                if(weather!= null && "ok".equals(weather.status))
                {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                    editor.putString("weather",response);
                    editor.apply();
                    showWeatherInfo(weather);
                }else{
                    Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_LONG).show();
                }

                mView.closeRefreshOropen(false);
            }

            @Override
            public void onFail() {
                Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_LONG).show();
                mView.closeRefreshOropen(false);
            }
        });
        loadBgPic();
    }

    private void showWeatherInfo(Weather weather)
    {
        mView.setWeatherInfo(weather);
    }


    private void loadBgPic()
    {
        modelBiz.getDataFromServer(Constant.IMAGEURL, null, new DataCallBack() {
            @Override
            public void onSuccess(String response) {
             SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("bgImg",response);
                editor.apply();
                mView.setBgPic(response);
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public void onRefresh() {
        requestWeather(globalweatherId);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.nav_button)
        {
            mView.openDrawlayoutOrclose("open");
        }
    }

    public  void refreshNewCity(String weatherId)
    {    globalweatherId = weatherId;
        mView.openDrawlayoutOrclose("close");
        mView.closeRefreshOropen(true);
        requestWeather(globalweatherId);
    }

}

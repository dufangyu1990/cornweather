package com.cornweather.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.cornweather.android.callback.DataCallBack;
import com.cornweather.android.gson.Weather;
import com.cornweather.android.model.ILogicModel;
import com.cornweather.android.model.LogicModelImp;
import com.cornweather.android.present.ActivityPresentImp;
import com.cornweather.android.service.AutoUpdateService;
import com.cornweather.android.util.Constant;
import com.cornweather.android.util.LogUtil;
import com.cornweather.android.util.Utility;


/**
 * Created by dufangyu on 2017/3/8.
 */

public class WeatherActivity extends ActivityPresentImp<WeatherView> implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {

    private ILogicModel modelBiz;
    private String globalweatherId;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter filter;
    private Intent serviceIntent;
    @Override
    public void afterViewCreate(Bundle savedInstance) {
        super.afterViewCreate(savedInstance);


        if(Build.VERSION.SDK_INT>=21)
        {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        filter = new IntentFilter(Constant.UPDATEBGIMGACTION);
        filter.addAction(Constant.UPDATEWEATHERACTION);
        localBroadcastManager.registerReceiver(mReceiver, filter);
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

            LogUtil.d("dfy","启动服务");
            serviceIntent= new Intent(this, AutoUpdateService.class);
            startService(serviceIntent);
        }else{
            globalweatherId = getIntent().getStringExtra("weather_id");
            mView.showNdHideWeather(false);
            requestWeather(globalweatherId);
        }



    }



    private void requestWeather(final String weatherId)
    {

//        LogUtil.d("dfy","传给后台weatherId = "+weatherId);
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
//        LogUtil.d("dfy","下拉刷新weatherId = "+globalweatherId);
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

    {
//        LogUtil.d("dfy","重新选择城市weatherId = "+weatherId);
        globalweatherId = weatherId;
        mView.openDrawlayoutOrclose("close");
        mView.closeRefreshOropen(true);
        requestWeather(globalweatherId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(mReceiver);
        stopService(serviceIntent);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("dfy","后台服务刷新天气和图片");
            String action = intent.getAction();
            if(action.equals(Constant.UPDATEWEATHERACTION))
            {
                Weather weather = (Weather) intent.getSerializableExtra("weatherinfo");
                showWeatherInfo(weather);
            }else if(action.equals(Constant.UPDATEBGIMGACTION))
            {
                String bgimg = intent.getStringExtra("bgimg");
                mView.setBgPic(bgimg);

            }
        }
    };

}

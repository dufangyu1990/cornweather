package com.cornweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.cornweather.android.callback.DataCallBack;
import com.cornweather.android.gson.Weather;
import com.cornweather.android.model.ILogicModel;
import com.cornweather.android.model.LogicModelImp;
import com.cornweather.android.util.Constant;
import com.cornweather.android.util.LogUtil;
import com.cornweather.android.util.Utility;

import static com.cornweather.android.util.Constant.UPDATEBGIMGACTION;
import static com.cornweather.android.util.Constant.UPDATEWEATHERACTION;

/**
 * Created by dufangyu on 2017/3/9.
 */

public class AutoUpdateService extends Service{


    private ILogicModel serviceBiz;

    private LocalBroadcastManager mLocalBroadcastManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        serviceBiz = new LogicModelImp();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updatePic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60*60*1000;//1小时刷新一次
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pendingIntent =PendingIntent.getService(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }


    private void updateWeather()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString  = preferences.getString("weather",null);
        if(weatherString!=null)
        {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = Constant.WEATHERBASEURL+"?city="+weatherId+"&key="+Constant.KEY;
            serviceBiz.getDataFromServer(weatherUrl, "", new DataCallBack() {
                @Override
                public void onSuccess(String response) {
                    Weather weather = Utility.handleWeatherResponse(response);
                    if(weather!= null && "ok".equals(weather.status))
                    {
                        Intent intent = new Intent();
                        intent.setAction(UPDATEWEATHERACTION);
                        intent.putExtra("weatherinfo", weather);
                        mLocalBroadcastManager.sendBroadcast(intent);
//                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
//                        editor.putString("weather",response);
//                        editor.apply();
                    }
                }

                @Override
                public void onFail() {

                }
            });
        }
    }



    private void updatePic()
    {
        serviceBiz.getDataFromServer(Constant.IMAGEURL, null, new DataCallBack() {
            @Override
            public void onSuccess(String response) {


                Intent intent = new Intent();
                intent.setAction(UPDATEBGIMGACTION);
                intent.putExtra("bgimg", response);
                mLocalBroadcastManager.sendBroadcast(intent);
//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("bgImg",response);
//                editor.apply();
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("dfy","service stop");
    }
}

package com.cornweather.android.model;

import android.os.Handler;
import android.os.Looper;

import com.cornweather.android.callback.DataCallBack;
import com.cornweather.android.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dufangyu on 2017/3/7.
 */

public   class LogicModelImp implements ILogicModel{

    private Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public void getDataFromServer(String url, String type, final DataCallBack callBack) {
        HttpUtil.sendOKHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFail();
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    final String result = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(result);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }



}

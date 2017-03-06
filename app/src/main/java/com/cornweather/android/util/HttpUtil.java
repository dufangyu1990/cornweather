package com.cornweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by dufangyu on 2017/3/6.
 */

public class HttpUtil {

    private static OkHttpClient client;

    public static void sendOKHttpRequest(String address,okhttp3.Callback callback)
    {
        OkHttpClient client = getOkHttpClinetInstance();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }


    public static OkHttpClient getOkHttpClinetInstance()
    {
        if(client ==null)
        {
            client = new OkHttpClient();
        }
        return client;
    }




}

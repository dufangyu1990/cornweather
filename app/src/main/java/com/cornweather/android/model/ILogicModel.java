package com.cornweather.android.model;

import com.cornweather.android.callback.DataCallBack;

/**
 * Created by dufangyu on 2017/3/7.
 */

public interface ILogicModel {
    void getDataFromServer(String url, String type, DataCallBack callBack);
}

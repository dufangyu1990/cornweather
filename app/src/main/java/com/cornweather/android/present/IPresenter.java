package com.cornweather.android.present;

import android.os.Bundle;

/**
 * Created by dufangyu on 2017/3/6.
 */

public interface IPresenter<T> {

    /**
     * 获取当前presenter泛型的类型
     * @return
     */

    Class<T> getViewClass();

    /**
     * View初始化之前可以在此方法做一些操作
     */
   void beforeViewCreate(Bundle savedInstance);

    /**
     * view初始化后做一些操作
     * @param savedInstance
     */
    void afterViewCreate(Bundle savedInstance);

}

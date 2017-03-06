package com.cornweather.android.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cornweather.android.present.IPresenter;

/**
 * Created by dufangyu on 2017/3/6.
 */

public interface IView {


    /**
     *根据 {@link getLayoutId}方法生成生成setContentView需要的根布局
     * @param inflater
     * @param container
     * @return
     */

    View create(LayoutInflater inflater, ViewGroup container);

    /**
     *返回当前视图需要的layout的id
     * @return
     */
    int getLayoutId();

    /**
     * Activity onCreate()方法调用完成之后被调用
     */
    void findViews();


    /**
     * 根据id获取view
     * @param id
     * @param <V>
     * @return
     */
    <V extends View> V findViewById(int id);


    /**
     * 绑定present
     * @param presenter
     */
    void bindPresenter(IPresenter presenter);

    /**
     * 绑定事件
     */
    void bindEvent();

}

package com.cornweather.android.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cornweather.android.present.IPresenter;

/**
 * Created by dufangyu on 2017/3/6.
 */

public abstract class ViewImp implements IView{

    protected View mRootView;
    protected IPresenter mPresenter;

    @Override
    public View create(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(getLayoutId(),container,false);
        findViews();
        return mRootView;
    }


    @Override
    public void findViews() {

    }

    @Override
    public <V extends View> V findViewById(int id) {
        return (V) mRootView.findViewById(id);
    }

    @Override
    public void bindPresenter(IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void bindEvent() {

    }
}

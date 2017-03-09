package com.cornweather.android.present;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cornweather.android.helper.GenericHelper;
import com.cornweather.android.view.IView;

/**
 * Created by dufangyu on 2017/3/6.
 */

public class ActivityPresentImp<T extends IView> extends AppCompatActivity implements IPresenter<T>{

    protected T mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeViewCreate(savedInstanceState);
        try {
            mView = getViewClass().newInstance();
            mView.bindPresenter(this);
            setContentView(mView.create(getLayoutInflater(),null));
            mView.bindEvent();
            afterViewCreate(savedInstanceState);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Class<T> getViewClass() {
        return GenericHelper.getViewClass(getClass());
    }

    @Override
    public void beforeViewCreate(Bundle savedInstance) {

    }

    @Override
    public void afterViewCreate(Bundle savedInstance) {

    }
}

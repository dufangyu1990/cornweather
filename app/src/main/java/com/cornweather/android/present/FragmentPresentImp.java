package com.cornweather.android.present;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cornweather.android.helper.GenericHelper;
import com.cornweather.android.view.IView;

/**
 * Created by dufangyu on 2017/3/6.
 */

public class FragmentPresentImp<T extends IView> extends Fragment implements IPresenter<T>{

    protected T mView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        beforeViewCreate(savedInstanceState);

        try {
            mView = getViewClass().newInstance();
            View view = mView.create(inflater, container);
            mView.bindPresenter(this);
            mView.bindEvent();
            afterViewCreate(savedInstanceState);
            return view;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
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

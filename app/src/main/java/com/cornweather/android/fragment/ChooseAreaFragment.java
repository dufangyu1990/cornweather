package com.cornweather.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.cornweather.android.R;
import com.cornweather.android.db.City;
import com.cornweather.android.db.Country;
import com.cornweather.android.db.Province;
import com.cornweather.android.present.FragmentPresentImp;
import com.cornweather.android.util.Constant;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dufangyu on 2017/3/6.
 */

public class ChooseAreaFragment extends FragmentPresentImp<AreaFragmentView> implements View.OnClickListener,AdapterView.OnItemClickListener{


    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<Country> countryList;
    //被选中的省份
    private Province selectedProvince;
    private City selectedCity;
    private Country selectedCountry;
    //当前选中的级别
    private int currentLevel;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView.initList(dataList);
        queryProvinces();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.back_button:
                if(currentLevel==Constant.LEVEL_COUNTRY)
                {

                }else if(currentLevel==Constant.LEVEL_CITY)
                {

                }
                break;
            default:
                break;
        }



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(currentLevel == Constant.LEVEL_PROVINCE)
        {

        }else if(currentLevel ==Constant.LEVEL_CITY)
        {

        }


    }



    private void queryProvinces()
    {
        mView.setTextNdButtonShow("中国",false);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size()>0)
        {
            dataList.clear();
            for(Province province:provinceList)
            {
                dataList.add(province.getProvinceName());
            }
            mView.notifyData();
            currentLevel = Constant.LEVEL_PROVINCE;
        }else{
            queryFromServer(Constant.URL);
        }

    }




    private void queryFromServer(String url)
    {
        mView.showProgressDialog();

    }



}

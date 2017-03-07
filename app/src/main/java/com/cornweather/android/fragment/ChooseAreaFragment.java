package com.cornweather.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.cornweather.android.R;
import com.cornweather.android.callback.DataCallBack;
import com.cornweather.android.db.City;
import com.cornweather.android.db.Country;
import com.cornweather.android.db.Province;
import com.cornweather.android.model.ILogicModel;
import com.cornweather.android.model.LogicModelImp;
import com.cornweather.android.present.FragmentPresentImp;
import com.cornweather.android.util.Constant;
import com.cornweather.android.util.LogUtil;
import com.cornweather.android.util.Utility;

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
    //当前选中的级别
    private int currentLevel;

    private ILogicModel logicModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView.initList(dataList);
        logicModel = new LogicModelImp();
        queryProvinces();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.back_button:
                if(currentLevel==Constant.LEVEL_COUNTRY)
                {
                    queryCities();
                }else if(currentLevel==Constant.LEVEL_CITY)
                {
                    queryProvinces();
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
            selectedProvince = provinceList.get(position);
            queryCities();
        }else if(currentLevel ==Constant.LEVEL_CITY)
        {
            selectedCity = cityList.get(position);
            queryCountries();
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
            queryFromServer(Constant.URL,Constant.PROVINCETYPE);
        }

    }



    private void queryCities()
    {
        mView.setTextNdButtonShow(selectedProvince.getProvinceName(),true);
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0)
        {
            dataList.clear();
            for(City city:cityList)
            {
                dataList.add(city.getCityName());
            }
            mView.notifyData();
            currentLevel = Constant.LEVEL_CITY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
//            queryFromServer("",Constant.CITYTYPE);
            queryFromServer(Constant.URL+"/"+provinceCode,Constant.CITYTYPE);
        }
    }




    private void queryCountries()
    {
        mView.setTextNdButtonShow(selectedCity.getCityName(),true);
        countryList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(Country.class);
        if(countryList.size()>0)
        {
            dataList.clear();
            for(Country country:countryList)
            {
                dataList.add(country.getCountryName());
            }
            mView.notifyData();
            currentLevel = Constant.LEVEL_COUNTRY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            queryFromServer(Constant.URL+"/"+provinceCode+"/"+cityCode,Constant.COUNTRYTYPE);
        }
    }



    private void queryFromServer(String url, final String type)
    {
        mView.showProgressDialog();
        logicModel.getDataFromServer(url, type, new DataCallBack() {
            @Override
            public void onSuccess(String response) {
                String responseText = response;
                boolean result =false;
                if(Constant.PROVINCETYPE.equals(type))
                {
                    result = Utility.handleProvinceResponse(responseText);
                }else if(Constant.CITYTYPE.equals(type))
                {
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if(Constant.COUNTRYTYPE.equals(type))
                {
                    result = Utility.handleCountryResponse(responseText,selectedCity.getId());
                }

                if(result)
                {
                    mView.closeProDialog();
                    if(Constant.PROVINCETYPE.equals(type))
                    {
                        queryProvinces();
                    }else if(Constant.CITYTYPE.equals(type))
                    {
                        queryCities();
                    }else if(Constant.COUNTRYTYPE.equals(type))
                    {
                        queryCountries();
                    }
                }
            }

            @Override
            public void onFail() {
                LogUtil.d("dfy","Thread = "+Thread.currentThread().getName());
                mView.closeProDialog();
                Toast.makeText(getContext(),"加载失败",Toast.LENGTH_LONG).show();
            }
        });
//        HttpUtil.sendOKHttpRequest(url, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mView.closeProDialog();
//                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseText = response.body().string();
//                boolean result =false;
//                if(Constant.PROVINCETYPE.equals(type))
//                {
//                    result = Utility.handleProvinceResponse(responseText);
//                }else if(Constant.CITYTYPE.equals(type))
//                {
//                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
//                }else if(Constant.COUNTRYTYPE.equals(type))
//                {
//                    result = Utility.handleCountryResponse(responseText,selectedCity.getId());
//                }
//
//                if(result)
//                {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mView.closeProDialog();
//                            if(Constant.PROVINCETYPE.equals(type))
//                            {
//                                queryProvinces();
//                            }else if(Constant.CITYTYPE.equals(type))
//                            {
//                                queryCities();
//                            }else if(Constant.COUNTRYTYPE.equals(type))
//                            {
//                                queryCountries();
//                            }
//
//                        }
//                    });
//                }
//
//            }
//        });

    }



}

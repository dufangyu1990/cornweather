package com.cornweather.android.util;

import android.text.TextUtils;

import com.cornweather.android.db.City;
import com.cornweather.android.db.Country;
import com.cornweather.android.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by dufangyu on 2017/3/7.
 */

public class Utility {


    /**
     * 解析省份数据
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response)
    {

        if(!TextUtils.isEmpty(response))
        {
            try {
                JSONArray array = new JSONArray(response);
                int length = array.length();
                for(int i =0;i<length;i++)
                {
                    JSONObject obj = array.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(obj.getString("name"));
                    province.setProvinceCode(obj.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    /**
     * 解析处理市级数据
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCityResponse(String response,int provinceId)
    {


        if(!TextUtils.isEmpty(response))
        {
            try {
                JSONArray array = new JSONArray(response);
                int length = array.length();
                for(int i =0;i<length;i++)
                {
                    JSONObject obj = array.getJSONObject(i);
                    City city = new City();
                    city.setCityName(obj.getString("name"));
                    city.setCityCode(obj.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }



    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountryResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    Country county = new Country();
                    county.setCountryName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}

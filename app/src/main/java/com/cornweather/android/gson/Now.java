package com.cornweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dufangyu on 2017/3/8.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }

}

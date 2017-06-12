package com.jiachabao.project.com.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Loki on 2017/5/31.
 */

public class HomeAds {

    @SerializedName("index")
    @Expose
    public int index;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("openurl")
    @Expose
    public String openUrl;

}

package com.jiachabao.project.com.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Loki on 2017/6/2.
 */

public class Minutes {
    @SerializedName("times")
    @Expose
    public String times;

    @SerializedName("curp")
    @Expose
    public int curp;

    @SerializedName("curvalue")
    @Expose
    public int curValue;

    @SerializedName("curvolume")
    @Expose
    public int curVolume;

    @SerializedName("updown")
    @Expose
    public float upDown;

    @SerializedName("updownrate")
    @Expose
    public String upDownRate;
}

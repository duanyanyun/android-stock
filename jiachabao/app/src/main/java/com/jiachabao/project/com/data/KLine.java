package com.jiachabao.project.com.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Loki on 2017/6/16.
 */

public class KLine {
    @SerializedName("timestamp")
    @Expose
    public String timeStamp;

    @SerializedName("highp")
    @Expose
    public String highp;

    @SerializedName("openp")
    @Expose
    public String openp;

    @SerializedName("lowp")
    @Expose
    public String lowp;

    @SerializedName("nowv")
    @Expose
    public String nowv;

    @SerializedName("curvol")
    @Expose
    public String curVol;

    @SerializedName("preclose")
    @Expose
    public String preClose;

    @SerializedName("updown")
    @Expose
    public String upDown;

    @SerializedName("updownrate")
    @Expose
    public String upDownRate;

}

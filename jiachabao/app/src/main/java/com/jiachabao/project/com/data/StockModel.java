package com.jiachabao.project.com.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loki on 2017/6/2.
 */

public class StockModel<T> {

    //打开类型
    @SerializedName("openstatus")
    @Expose
    public int openStatus;
    //交易状态
    @SerializedName("tradestatus")
    @Expose
    public int tradeStatus;
    //交易状态提示
    @SerializedName("tradestatustip")
    @Expose
    public String tradeStatusTip;
    //交易信息文本
    @SerializedName("tradestatustext")
    @Expose
    public String tradeStatusText;

    @SerializedName("contractid")
    @Expose
    public int contractId;
    //标志
    @SerializedName("symbol")
    @Expose
    public String symbol;
    //标志名称
    @SerializedName("symbolname")
    @Expose
    public String symbolName;
    //最后一次交易
    @SerializedName("lasttradingdate")
    @Expose
    public int lasttradingDate;
    //当前交易值
    @SerializedName("nowv")
    @Expose
    public float nowv;

    @SerializedName("updown")
    @Expose
    public float updown;

    @SerializedName("updownrate")
    @Expose
    public String updownRate;

    @SerializedName("highp")
    @Expose
    public float highp;

    @SerializedName("openp")
    @Expose
    public float openp;

    @SerializedName("lowp")
    @Expose
    public float lowp;

    @SerializedName("preclose")
    @Expose
    public float preClose;

    @SerializedName("bidp")
    @Expose
    public float bidp;

    @SerializedName("askp")
    @Expose
    public float askp;

    @SerializedName("bids")
    @Expose
    public int bids;

    @SerializedName("asks")
    @Expose
    public int asks;

    @SerializedName("tradesize")
    @Expose
    public int tradeSize;
    //波动
    @SerializedName("fluctuate")
    @Expose
    public int fluctuate;
    //涨速
    @SerializedName("changespeed")
    @Expose
    public String changespeed;
    //时间戳
    @SerializedName("timestamp")
    @Expose
    public String timestamp;
    //当前显示数量
    @SerializedName("count")
    @Expose
    public int count;
    //总数量
    @SerializedName("scount")
    @Expose
    public int scount;

    @SerializedName("daynight")
    @Expose
    public String daynight;

    @SerializedName("place")
    @Expose
    public String place;

    @SerializedName("ba")
    @Expose
    public String ba;

    @SerializedName("timeaxistext")
    @Expose
    public String[] timeaxisText;

    @SerializedName("timeaxisindex")
    @Expose
    public int[] timeaxisIndex;

    @SerializedName("contracttypeid")
    @Expose
    public int contractTypeId;

    @SerializedName("timedata")
    @Expose
    public ArrayList<T> TimeData;


}

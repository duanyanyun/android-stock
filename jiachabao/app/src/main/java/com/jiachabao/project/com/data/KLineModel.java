package com.jiachabao.project.com.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Loki on 2017/6/19.
 */

public class KLineModel<T> {

    //打开类型
    @SerializedName("openstatus")
    @Expose
    public String openStatus;
    //交易状态
    @SerializedName("tradestatus")
    @Expose
    public String tradeStatus;
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
    public String contractId;
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
    public String lasttradingDate;
    //当前交易值
    @SerializedName("nowv")
    @Expose
    public String nowv;

    @SerializedName("updown")
    @Expose
    public String updown;

    @SerializedName("updownrate")
    @Expose
    public String updownRate;

    @SerializedName("highp")
    @Expose
    public String highp;

    @SerializedName("openp")
    @Expose
    public String openp;

    @SerializedName("lowp")
    @Expose
    public String lowp;

    @SerializedName("preclose")
    @Expose
    public String preClose;

    @SerializedName("bidp")
    @Expose
    public String bidp;

    @SerializedName("askp")
    @Expose
    public String askp;

    @SerializedName("bids")
    @Expose
    public String bids;

    @SerializedName("asks")
    @Expose
    public String asks;

    @SerializedName("tradesize")
    @Expose
    public String tradeSize;
    //波动
    @SerializedName("fluctuate")
    @Expose
    public String fluctuate;
    //涨速
    @SerializedName("changespeed")
    @Expose
    public String changespeed;

    //当前显示数量
    @SerializedName("count")
    @Expose
    public String count;


    @SerializedName("place")
    @Expose
    public String place;

    @SerializedName("ba")
    @Expose
    public String ba;

    @SerializedName("contracttypeid")
    @Expose
    public String contractTypeId;

    @SerializedName("timedata")
    @Expose
    public ArrayList<T> TimeData; //String TimeData; //

}

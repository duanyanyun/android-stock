package com.jiachabao.project.com.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Loki on 2017/5/31.
 */

public class Contract {
    @SerializedName("nowv")
    @Expose
    public String nowv;

    @SerializedName("updownrate")
    @Expose
    public String updownrate;

    @SerializedName("openstatus")
    @Expose
    public int openStatus;

    @SerializedName("openstatustext")
    @Expose
    public String openStatusText;

    @SerializedName("contractid")
    @Expose
    public int contractId;

    @SerializedName("symbol")
    @Expose
    public String symbol;

    @SerializedName("symbolname")
    @Expose
    public String symbolName;

    @SerializedName("lasttradingdate")
    @Expose
    public String lastTradingDate;

    @SerializedName("contracttypeid")
    @Expose
    public int contractTypeId;

    @SerializedName("introduce")
    @Expose
    public String introduce;

}

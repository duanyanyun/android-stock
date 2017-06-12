package com.jiachabao.project.com.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Loki on 2017/5/31.
 */

public class HttpResultList<T> {

    @SerializedName("status")
    @Expose
    private  String status;

    @SerializedName("info")
    @Expose
    private  String info;

    @SerializedName("data")
    @Expose
    private ArrayList<T> data;

    public ArrayList<T> getData(){
        return this.data;
    }

    public String getMessage(){
        return this.info;
    }

    public String getStatus(){
        return  this.status;
    }
}

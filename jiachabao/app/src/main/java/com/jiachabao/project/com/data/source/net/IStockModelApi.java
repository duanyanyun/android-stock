package com.jiachabao.project.com.data.source.net;

import com.jiachabao.project.com.data.HomeAds;
import com.jiachabao.project.com.data.HttpResult;
import com.jiachabao.project.com.data.KLine;
import com.jiachabao.project.com.data.KLineModel;
import com.jiachabao.project.com.data.Minutes;
import com.jiachabao.project.com.data.StockModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Loki on 2017/6/2.
 */

public interface IStockModelApi {

    //获取当前股票信息
    @GET("/mktdata/futuresshare.ashx")
    Observable<HttpResult<StockModel<Minutes>>> getStockModel(@Query("usertoken") String usertoken,
                                                              @Query("version") String version,
                                                              @Query("packtype") String packtype,
                                                              @Query("proxyid") String proxyid,
                                                              @Query("contractid") String contractid,
                                                              @Query("count") String count,
                                                              @Query("ba") String ba);

    @GET("/mktdata/futureskline.ashx")
    Observable<HttpResult<KLineModel<KLine>>> getKLineStockModel(@Query("usertoken") String usertoken,
                                                                 @Query("version") String version,
                                                                 @Query("packtype") String packtype,
                                                                 @Query("proxyid") String proxyid,
                                                                 @Query("contractid") String contractid,
                                                                 @Query("count") String count,
                                                                 @Query("type") String type);

    @GET("/mktdata/futureskline.ashx")
    Observable<HttpResult> getKLineStockModel1(@Query("usertoken") String usertoken,
                                                                 @Query("version") String version,
                                                                 @Query("packtype") String packtype,
                                                                 @Query("proxyid") String proxyid,
                                                                 @Query("contractid") String contractid,
                                                                 @Query("count") String count,
                                                                 @Query("type") String type);


    //获取当前股票信息
    @GET("/mktdata/futuresshare.ashx")
    Observable<HttpResult<StockModel<Minutes>>> getStockModel(@Query("usertoken") String usertoken,
                                                              @Query("version") String version,
                                                              @Query("packtype") String packtype,
                                                              @Query("proxyid") String proxyid,
                                                              @Query("contractid") String contractid,
                                                              @Query("count") String count,
                                                              @Query("ba") String ba,
                                                              @Query("time") String time);

}

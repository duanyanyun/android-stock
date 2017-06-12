package com.jiachabao.project.com.data.source.net;

import com.jiachabao.project.com.data.Contract;
import com.jiachabao.project.com.data.HomeAds;
import com.jiachabao.project.com.data.HttpResult;
import com.jiachabao.project.com.data.HttpResultList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Loki on 2017/5/31.
 */

public interface IHomeApi {

    @GET("/public/homeads.ashx")
    Observable<HttpResultList<HomeAds>> getAds(@Query("version") String version, @Query("packtype") String packtype);

    @GET("/mktdata/contractdatalist.ashx")
    Observable<HttpResultList<Contract>> getContractList(@Query("version") String version, @Query("packtype") String packtype);

}

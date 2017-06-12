package com.jiachabao.project.com.data.source.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jiachabao.project.com.data.HttpResult;
import com.jiachabao.project.com.data.Minutes;
import com.jiachabao.project.com.data.StockModel;
import com.jiachabao.project.com.data.source.remote.HomeRemoteDataSource;
import com.jiachabao.project.com.data.source.remote.StockModelRemoteDataSource;

import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Loki on 2017/6/2.
 */

public class StockModelRepository {

    public final StockModelRemoteDataSource stockModelRemoteDataSource;
    private static StockModelRepository INSTANCE;

    public StockModelRepository(@NonNull Context context){
        checkNotNull(context);
        stockModelRemoteDataSource=new StockModelRemoteDataSource();
    }

    public static StockModelRepository getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new StockModelRepository(context);
        }
        return INSTANCE;
    }


    public Observable<HttpResult<StockModel<Minutes>>> getStockModel(String usertoken,String version,String packtype,String proxyid,String contractid,String count,String ba){
        return stockModelRemoteDataSource.getStockModel(usertoken,version,packtype,proxyid,contractid,count,ba);
    }

    public Observable<HttpResult<StockModel<Minutes>>> getStockModel(String usertoken,String version,String packtype,String proxyid,String contractid,String count,String ba,String time){
        return stockModelRemoteDataSource.getStockModel(usertoken,version,packtype,proxyid,contractid,count,ba,time);
    }


}

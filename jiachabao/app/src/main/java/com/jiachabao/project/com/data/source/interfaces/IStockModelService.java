package com.jiachabao.project.com.data.source.interfaces;

import android.support.annotation.NonNull;

import com.jiachabao.project.com.data.HttpResult;
import com.jiachabao.project.com.data.Minutes;
import com.jiachabao.project.com.data.StockModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Loki on 2017/6/2.
 */

public interface IStockModelService {

    //获取当前股票信息
    Observable<HttpResult<StockModel<Minutes>>> getStockModel(@NonNull String usertoken,
                                                              @NonNull String version,
                                                              @NonNull String packtype,
                                                              @NonNull String proxyid,
                                                              @NonNull String contractid,
                                                              @NonNull String count,
                                                              @NonNull String ba);

}

package com.jiachabao.project.com.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.jiachabao.project.com.Constant;
import com.jiachabao.project.com.data.Contract;
import com.jiachabao.project.com.data.HttpResult;
import com.jiachabao.project.com.data.HttpResultList;
import com.jiachabao.project.com.data.KLine;
import com.jiachabao.project.com.data.KLineModel;
import com.jiachabao.project.com.data.Minutes;
import com.jiachabao.project.com.data.StockModel;
import com.jiachabao.project.com.data.source.interfaces.IStockModelService;
import com.jiachabao.project.com.data.source.net.IHomeApi;
import com.jiachabao.project.com.data.source.net.IStockModelApi;
import com.jiachabao.project.com.util.RetrofitUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Loki on 2017/6/2.
 */

public class StockModelRemoteDataSource implements IStockModelService {
    @Override
    public Observable<HttpResult<StockModel<Minutes>>> getStockModel(@NonNull String usertoken, @NonNull String version,
                                                                     @NonNull String packtype, @NonNull String proxyid,
                                                                     @NonNull String contractid, @NonNull String count, @NonNull String ba) {
        return RetrofitUtils.getInstance().getService(IStockModelApi.class, Constant.url).getStockModel(usertoken, version,packtype,proxyid,contractid,count,ba)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<HttpResult<StockModel<Minutes>>>() {
                    @Override
                    public void call(HttpResult<StockModel<Minutes>> model) {
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<HttpResult<KLineModel<KLine>>> getKLineStockModel(@NonNull String usertoken, @NonNull String version, @NonNull String packtype, @NonNull String proxyid,
                                                                        @NonNull String contractid, @NonNull String count, @NonNull String type) {
        return RetrofitUtils.getInstance().getService(IStockModelApi.class, Constant.url).getKLineStockModel(usertoken, version,packtype,proxyid,contractid,count,type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<HttpResult<KLineModel<KLine>>>() {
                    @Override
                    public void call(HttpResult<KLineModel<KLine>> model) {
                    }
                }).observeOn(AndroidSchedulers.mainThread());

    }


    public Observable<HttpResult<StockModel<Minutes>>> getStockModel(@NonNull String usertoken, @NonNull String version,
                                                                     @NonNull String packtype, @NonNull String proxyid,
                                                                     @NonNull String contractid, @NonNull String count, @NonNull String ba,@NonNull String time) {
        return RetrofitUtils.getInstance().getService(IStockModelApi.class, Constant.url).getStockModel(usertoken, version,packtype,proxyid,contractid,count,ba,time)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<HttpResult<StockModel<Minutes>>>() {
                    @Override
                    public void call(HttpResult<StockModel<Minutes>> model) {
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
}

package com.jiachabao.project.com.data.source.remote;

import android.support.annotation.NonNull;

import com.jiachabao.project.com.Constant;
import com.jiachabao.project.com.data.Contract;
import com.jiachabao.project.com.data.HomeAds;
import com.jiachabao.project.com.data.HttpResult;
import com.jiachabao.project.com.data.HttpResultList;
import com.jiachabao.project.com.data.source.interfaces.IHomeService;
import com.jiachabao.project.com.data.source.net.IHomeApi;
import com.jiachabao.project.com.util.RetrofitUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Loki on 2017/5/31.
 */

public class HomeRemoteDataSource implements IHomeService {

    @Override
    public Observable<HttpResultList<HomeAds>> getAds(@NonNull String version, @NonNull String packtype) {
        return RetrofitUtils.getInstance().getService(IHomeApi.class,Constant.urlPay).getAds(version, packtype)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<HttpResultList<HomeAds>>() {
                    @Override
                    public void call(HttpResultList<HomeAds> model) {
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<HttpResultList<Contract>> getContractList(@NonNull String version, @NonNull String packtype) {
        return RetrofitUtils.getInstance().getService(IHomeApi.class, Constant.url).getContractList(version, packtype)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<HttpResultList<Contract>>() {
                    @Override
                    public void call(HttpResultList<Contract> model) {
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }


}

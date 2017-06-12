package com.jiachabao.project.com.data.source.interfaces;

import android.support.annotation.NonNull;

import com.jiachabao.project.com.data.Contract;
import com.jiachabao.project.com.data.HomeAds;
import com.jiachabao.project.com.data.HttpResult;
import com.jiachabao.project.com.data.HttpResultList;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Loki on 2017/5/31.
 */

public interface IHomeService {

    Observable<HttpResultList<HomeAds>> getAds(@NonNull String version,@NonNull String packtype);

    Observable<HttpResultList<Contract>> getContractList(@NonNull String version, @NonNull String packtype);

}

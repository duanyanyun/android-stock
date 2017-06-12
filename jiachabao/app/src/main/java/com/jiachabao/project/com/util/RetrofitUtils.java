package com.jiachabao.project.com.util;

import com.jiachabao.project.com.Constant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Loki on 2017/5/26.
 */

public class RetrofitUtils {


    private static RetrofitUtils sInstance;
    private Retrofit retrofit;
    public static RetrofitUtils getInstance() {
        if (sInstance == null) {
            sInstance = new RetrofitUtils();
        }
        return sInstance;
    }

    public  <T>T getService(final Class<T> tClass,String url){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(url).client(OkHttpUtils.getOkHttpClient()).build();
        return  retrofit.create(tClass);
    }


}

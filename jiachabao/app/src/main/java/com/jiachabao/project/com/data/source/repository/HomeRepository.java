package com.jiachabao.project.com.data.source.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jiachabao.project.com.data.source.remote.HomeRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Loki on 2017/5/31.
 */

public class HomeRepository {


    public final HomeRemoteDataSource homeRemoteDataSource;
    private static HomeRepository INSTANCE;

    public HomeRepository(@NonNull Context context){
        checkNotNull(context);
        homeRemoteDataSource=new HomeRemoteDataSource();
    }

    public static HomeRepository getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HomeRepository(context);
        }
        return INSTANCE;
    }




}

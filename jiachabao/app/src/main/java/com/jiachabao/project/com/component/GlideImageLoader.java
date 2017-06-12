package com.jiachabao.project.com.component;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Loki on 2017/5/27.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Log.d("测试",""+String.valueOf(path));
        Glide.with(context.getApplicationContext())
                .load(path)
                .into(imageView);
    }
}

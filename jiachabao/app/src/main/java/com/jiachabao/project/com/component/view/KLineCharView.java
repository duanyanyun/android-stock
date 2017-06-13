package com.jiachabao.project.com.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.jiachabao.project.com.util.DisplayUtil;

/**
 * Created by Loki on 2017/6/13.
 */

public class KLineCharView extends RelativeLayout {
    public KLineCharView(Context context) {
        super(context);
        init();
    }

    public KLineCharView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KLineCharView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float clearanceW= DisplayUtil.px2dip(getContext(),2);
        float itemW= DisplayUtil.px2dip(getContext(),5);
        Log.d("测试K","W:"+(w)+"   H:"+h+"     ItemCount:"+(w/(itemW+clearanceW*2)));
    }



}

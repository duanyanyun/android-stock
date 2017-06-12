package com.jiachabao.project.com.component;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Loki on 2017/6/5.
 */

public class MyCustomFormatter implements IAxisValueFormatter {
    protected String[] mMonths;

    public MyCustomFormatter(String[] _mMonths){
        this.mMonths=_mMonths;
    }
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
//        float percent = value / axis.mAxisRange;
//        Log.d("测试","value:"+value+"---------percent:"+percent+"");
//        int index=(int) (mMonths.length * percent);
//        if(index>mMonths.length-1){
//            return mMonths[mMonths.length-1];
//        }
        if(value>0&&value<=300){
            return mMonths[1];
        }else if(value>300&&value<=600) {
            return mMonths[2];
        }else if(value>600&&value<=900) {
            return mMonths[3];
        }else if(value>900&&value<=1200) {
            return mMonths[4];
        }else {
            return mMonths[0];
        }
    }
}

package com.jiachabao.project.com.trading;


import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jiachabao.project.com.R;
import com.jiachabao.project.com.component.view.LineCharView;
import com.jiachabao.project.com.data.HttpResult;
import com.jiachabao.project.com.data.Minutes;
import com.jiachabao.project.com.data.StockModel;
import com.jiachabao.project.com.data.source.repository.StockModelRepository;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * A fragment with a Google +1 button.
 */
public class TradingFragment extends Fragment {

    private StockModelRepository stockModelRepository;
    private View view;
    private LineCharView textScrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_trading, container, false);
        textScrollView= (LineCharView)view.findViewById(R.id.tsv_view);
        ArrayList<String> list=new ArrayList<>();
        list.add("08:00");
        list.add("13:00");
        list.add("18:00");
        list.add("23:00");
        list.add("05:00");
        textScrollView.setLineCount(1260);
        textScrollView.setLabels(list);

        initData();

        final ImageView imageView=(ImageView)view.findViewById(R.id.iv_view);
        final Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.img_animation);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.startAnimation(animation);
            }
        });

        return view;
    }

    private void  initData(){
        stockModelRepository= StockModelRepository.getInstance(view.getContext());
        stockModelRepository.getStockModel("","9.9.9","775","0",String.valueOf(4925),"0","2").subscribe(new Subscriber<HttpResult<StockModel<Minutes>>>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(HttpResult<StockModel<Minutes>> result) {
                ArrayList<Minutes> data=result.getData().TimeData;
                ArrayList<Float> curps=new ArrayList<Float>();
                ArrayList<Float> updowns=new ArrayList<Float>();
                float minUpdown=0,maxUpdown=0,barMax=0;
                for (int i=data.size()-1,j=0;i>=0;i--,j++){
                    Minutes stock= data.get(i);
                    float curp=Float.valueOf(stock.curp)/100;
                    curps.add(curp);
                    if(i==0){
                        minUpdown=curp;
                    }
                    Log.d("测试",""+curp);
                    minUpdown=Math.min(curp, minUpdown);
                    maxUpdown=Math.max(curp, maxUpdown);
                    barMax=Math.max(stock.curVolume, barMax);
                    updowns.add(Float.valueOf(stock.curVolume));
                }
                textScrollView.setMaxCurp(result.getData().highp,result.getData().lowp,0.56f,-0.69f,barMax,result.getData().preClose);
                textScrollView.setData(curps);
                textScrollView.setBarData(updowns);
            }
        });
    }


}

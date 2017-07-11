package com.jiachabao.project.com.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiachabao.project.com.Constant;
import com.jiachabao.project.com.R;
import com.jiachabao.project.com.component.view.KLineCharView;
import com.jiachabao.project.com.component.view.LineCharView;
import com.jiachabao.project.com.data.HttpResult;
import com.jiachabao.project.com.data.KLine;
import com.jiachabao.project.com.data.KLineModel;
import com.jiachabao.project.com.data.Minutes;
import com.jiachabao.project.com.data.StockModel;
import com.jiachabao.project.com.data.source.net.IStockModelApi;
import com.jiachabao.project.com.data.source.repository.StockModelRepository;
import com.jiachabao.project.com.util.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private View view;
    private StockModelRepository stockModelRepository;
    private KLineCharView lineCharView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_user, container, false);
        stockModelRepository= StockModelRepository.getInstance(view.getContext());
        lineCharView=(KLineCharView)view.findViewById(R.id.klcv_char_view);
        //https://fd.qihuoniu.com/mktdata/futureskline.ashx?usertoken=X7KpjyUPIBv_FzZgb1RNvH4_G6OleTmYMe1-_Ra-pMJzp06uI-qOE6lLuM_qtsKa&version=9.9.9&packtype=775&proxyid=36&contractid=4918&count=10&type=1&ba=2
        getDate();//
        return view;
    }

    private void getDate(){

       stockModelRepository.getKLineStockModel("jnL2E4iiXLH4jyvLzGDzISoIxdSseMWkKnSAA9odlZuE103MrZjt0lI8RFz0pyVp","9.9.9","775","36","4925","100","2")
                .subscribe(new Subscriber<HttpResult<KLineModel<KLine>>>() {
            @Override
            public void onCompleted() {
                Log.d("测试K","--------------------onCompleted:");
            }
            @Override
            public void onError(Throwable e) {
                Log.d("测试K","--------------------onError:"+e.getMessage()+""+e.getLocalizedMessage());
            }
            @Override
            public void onNext(HttpResult<KLineModel<KLine>> result) {
                Log.d("测试K","--------------------result:"+result.getData().TimeData.size());
                float minUpdown=0,maxUpdown=0,barMax=0;
                ArrayList<KLine> listArr=new ArrayList<KLine>();
                for (int i=result.getData().TimeData.size()-1,j=0;i>=0;j++,i--){
                    KLine model=result.getData().TimeData.get(i);
                    float curp=Float.valueOf(model.highp)/100;
                    float curp1=Float.valueOf(model.openp)/100;
                    if(i==0){
                        minUpdown=curp;
                    }
                    minUpdown=Math.min(curp, minUpdown);
                    maxUpdown=Math.max(curp, maxUpdown);

                    minUpdown=Math.min(curp1, minUpdown);
                    maxUpdown=Math.max(curp1, maxUpdown);

                    barMax=Math.max(Float.valueOf(model.curVol), barMax);
                    listArr.add(model);
                }

                Log.d("测试K","-----minUpdown:"+minUpdown+"    maxUpdown:"+maxUpdown);
                //lineCharView.setMaxCurp(Float.valueOf(maxUpdown),Float.valueOf(minUpdown),barMax,Float.valueOf(result.getData().preClose));
                //setMaxCurp
                lineCharView.setData(listArr);
               // Log.d("测试K","----"+result.getData().TimeData.get(0).timeStamp);
            }
        });



    }

}

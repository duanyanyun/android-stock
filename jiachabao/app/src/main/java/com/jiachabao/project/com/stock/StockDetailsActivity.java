package com.jiachabao.project.com.stock;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jiachabao.project.com.R;
import com.jiachabao.project.com.data.HttpResult;
import com.jiachabao.project.com.data.Minutes;
import com.jiachabao.project.com.data.StockModel;
import com.jiachabao.project.com.data.source.repository.StockModelRepository;
import com.jiachabao.project.com.databinding.ActivityStockDetailsBinding;
import com.jiachabao.project.com.util.ActivityUtils;
import com.jiachabao.project.com.util.DisplayUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Subscriber;

public class StockDetailsActivity extends AppCompatActivity {

    private ActivityStockDetailsBinding binding;
    private int contractId;
    private StockModelRepository stockModelRepository;
    private String userToken="",userId="0";
    private Handler handler;
    private Runnable runnable;
    private TimeStockFragment timeStockFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_stock_details);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_stock_details);
        contractId= getIntent().getIntExtra("contract_id",0);
        stockModelRepository=StockModelRepository.getInstance(this);
        initView();

    }

    private void initView(){
        timeStockFragment=TimeStockFragment.newInstance();
        findOrCreateViewFragment(timeStockFragment);
        binding.ivBackBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        stockModelRepository.getStockModel(userToken,"9.9.9","775",userId,String.valueOf(contractId),"0","2").subscribe(new Subscriber<HttpResult<StockModel<Minutes>>>() {
            @Override
            public void onCompleted() {
                Log.d("测试","------------onCompleted:"+contractId);
                loadHandler();
            }
            @Override
            public void onError(Throwable e) {
                Log.d("测试","------------onError:"+contractId+"  "+e.getMessage());
            }
            @Override
            public void onNext(HttpResult<StockModel<Minutes>> result) {
                Log.d("测试","------------onNext:"+contractId);
                initData(result.getData());
                timeStockFragment.setData(result.getData());
            }
        });
        int tabWidth = (getWindowManager().getDefaultDisplay().getWidth()- DisplayUtil.dip2px(this,30))/5;
        ViewGroup.LayoutParams params= binding.tabTimeLine.getLayoutParams();
        params.width=tabWidth;
        binding.tabTimeLine.setLayoutParams(params);
        binding.tvTimeSharing.setBackgroundColor(getResources().getColor(R.color.color_f0f0f0));

        binding.tvTimeSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLine(v);
            }
        });
        binding.tvOneTimeSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLine(v);
            }
        });
        binding.tvFiveTimeSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLine(v);
            }
        });
        binding.tvDayk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLine(v);
            }
        });
        binding.tvMoreTimeSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLine(v);
            }
        });

    }



    private void findOrCreateViewFragment(Fragment fragment) {
       // TimeStockFragment timeStockFragment = TimeStockFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content, fragment);
        transaction.commit();
    }

    private void updateLine(View v){
        Log.d("测试","------------getLeft:"+v.getLeft());
        FrameLayout.LayoutParams params= (FrameLayout.LayoutParams)binding.tabTimeLine.getLayoutParams();
        params.setMargins(v.getLeft(),0,0,0);
        binding.tabTimeLine.setLayoutParams(params);
        v.setBackgroundColor(getResources().getColor(R.color.color_f0f0f0));
       // binding.tabTimeLine.setPadding(v.getLeft(),0,0,0);
    }

    private void initData(StockModel<Minutes> data){
        binding.tvTitle.setText(data.symbolName);
        binding.tvSymbolName.setText(data.symbolName+"  "+data.symbol+data.lasttradingDate);
        binding.tvTradeStatusText.setText(data.tradeStatusText);

        binding.tvNowv.setText(String.valueOf(data.nowv));
        binding.tvCurrentGains.setText(data.updown+"   "+data.updownRate);
        if(data.updown>=0){
            binding.tvNowv.setTextColor(getResources().getColor(R.color.color_fa6268));
            binding.tvCurrentGains.setTextColor(getResources().getColor(R.color.color_fa6268));
        }else {
            binding.tvNowv.setTextColor(getResources().getColor(R.color.color_1dbd7d));
            binding.tvCurrentGains.setTextColor(getResources().getColor(R.color.color_1dbd7d));
        }

        binding.tvHighp.setText(String.valueOf(data.highp));
        binding.tvLowp.setText(String.valueOf(data.lowp));
        binding.tvFluctuate.setText(String.valueOf(data.fluctuate));

        binding.tvOpenp.setText(String.valueOf(data.openp));
        binding.tvPreclose.setText(String.valueOf(data.preClose));
        binding.tvChangeSpeed.setText(String.valueOf(data.changespeed));

        //买涨数量
        binding.tvRiseNumber.setText(String.valueOf(data.bids));
        //买跌数量
        binding.tvDropNumber.setText(String.valueOf(data.asks));
        int bids= data.bids>0?data.bids:1;
        int drop= data.asks>0?data.asks:1;
        ViewGroup.LayoutParams params= binding.riseView.getLayoutParams();
        params.width=bids;
        binding.riseView.setLayoutParams(params);
        ViewGroup.LayoutParams dropParams= binding.dropView.getLayoutParams();
        dropParams.width=drop;
        binding.dropView.setLayoutParams(dropParams);
    }

    private void loadHandler(){
        handler=  new Handler();
        runnable= new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };
        handler.postDelayed(runnable, 500);
    }

    public void getData(){
        DateFormat format=new SimpleDateFormat("yyyyMMddHHmm00");
        final String time=format.format(new Date());
        stockModelRepository.getStockModel(userToken,"9.9.9","775",userId,String.valueOf(contractId),"1","2",time).subscribe(new Subscriber<HttpResult<StockModel<Minutes>>>() {
            @Override
            public void onCompleted() {
                handler.postDelayed(runnable, 1000);
            }
            @Override
            public void onError(Throwable e) {
                Log.d("测试","------------onError:"+contractId+"  "+e.getMessage());
            }
            @Override
            public void onNext(HttpResult<StockModel<Minutes>> result) {
                initData(result.getData());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null&&runnable!=null){
            handler.removeCallbacks(runnable);
        }
    }


}

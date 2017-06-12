package com.jiachabao.project.com.home;


import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.jiachabao.project.com.R;
import com.jiachabao.project.com.component.GlideImageLoader;
import com.jiachabao.project.com.component.adapter.CommonAdapter;
import com.jiachabao.project.com.component.adapter.ViewHolder;
import com.jiachabao.project.com.data.Contract;
import com.jiachabao.project.com.data.HomeAds;
import com.jiachabao.project.com.data.HttpResult;
import com.jiachabao.project.com.data.HttpResultList;
import com.jiachabao.project.com.data.source.repository.HomeRepository;
import com.jiachabao.project.com.databinding.FragmentHomeBinding;
import com.jiachabao.project.com.stock.StockDetailsActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * A fragment with a Google +1 button.
 */
public class HomeFragment extends Fragment implements OnBannerListener {

    private View view;
    private FragmentHomeBinding binding;
    private HomeRepository repository;
    private ArrayList<HomeAds> adList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        view=binding.getRoot();
        initView();
        return view;
    }

    private void initView(){

        repository= HomeRepository.getInstance(view.getContext());
        repository.homeRemoteDataSource.getAds("9.9.9","775").subscribe(new Subscriber<HttpResultList<HomeAds>>() {
            @Override
            public void onCompleted() {
                Log.d("测试","----------onCompleted");
            }
            @Override
            public void onError(Throwable e) {
                Log.d("测试",""+e.getMessage());
            }
            @Override
            public void onNext(HttpResultList<HomeAds> result) {
                Log.d("测试",""+result.getMessage());
                loadBannar(result);
            }
        });
        repository.homeRemoteDataSource.getContractList("9.9.9","775").subscribe(new Subscriber<HttpResultList<Contract>>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(HttpResultList<Contract> contractHttpResultList) {
                loadListView(contractHttpResultList);
            }
        });

        binding.llImitationTrading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.llNoviceRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.llCustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.rlRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        binding.rlRefresh.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新...");
        binding.rlRefresh.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        binding.rlRefresh.getLoadingLayoutProxy(true, false).setReleaseLabel("松开刷新...");
        binding.rlRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //getData();
            }
        });
    }

    private void loadBannar(HttpResultList<HomeAds> data){
        binding.bannarAt.setIndicatorGravity(BannerConfig.CENTER);
        binding.bannarAt.setOnBannerListener(this);
        adList=data.getData();
        ArrayList<String> imgs=new ArrayList<String>();
        for (HomeAds model:adList) {
            imgs.add(model.url);
        }
        binding.bannarAt.setImages(imgs)
                .setImageLoader(new GlideImageLoader())
                .start();
    }

    private void loadListView(final HttpResultList<Contract> data){
        binding.lvTradingList.setAdapter(new CommonAdapter<Contract>(view.getContext(),data.getData(),R.layout.home_trading_item) {
            @Override
            public void convert(ViewHolder helper, Contract item, int position) {
                TextView openText= helper.getView(R.id.tv_open_status_text);
                TextView updownrateText= helper.getView(R.id.tv_updownrate);
                TextView nowvText= helper.getView(R.id.tv_nowv);


                openText.setText(item.openStatusText);
                if(item.openStatus==1) {
                    openText.setTextColor(getResources().getColor(R.color.color_ffffff));
                    openText.setBackgroundDrawable(getResources().getDrawable(R.drawable.trading_status_h));
                }else {
                    openText.setTextColor(getResources().getColor(R.color.color_999999));
                    openText.setBackgroundDrawable(getResources().getDrawable(R.drawable.trading_status_n));
                }
                helper.setText(R.id.tv_introduce,item.introduce);
                helper.setText(R.id.tv_symbol_name,item.symbolName);
                String nowv=item.updownrate.replace("%","");
                updownrateText.setText(item.updownrate);
                nowvText.setText(item.nowv);
                if(Float.valueOf(nowv)>0) {
                    updownrateText.setTextColor(getResources().getColor(R.color.color_fa6268));
                    nowvText.setTextColor(getResources().getColor(R.color.color_fa6268));
                }else {
                    updownrateText.setTextColor(getResources().getColor(R.color.color_1dbd7d));
                    nowvText.setTextColor(getResources().getColor(R.color.color_1dbd7d));
                }
            }
        });
        binding.lvTradingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contract model= data.getData().get(i);
                Log.d("测试","-------contractId:"+model.contractId);
                Intent intent=new Intent(binding.getRoot().getContext(), StockDetailsActivity.class);
                intent.putExtra("contract_id",model.contractId);
                startActivity(intent);
            }
        });
        setListViewHeightBasedOnChildren();
    }

    @Override
    public void OnBannerClick(int position) {
        HomeAds item= adList.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(item.openUrl));
        startActivity(intent);
    }

     //计算listView高度
    public void setListViewHeightBasedOnChildren() {
        // 获取ListView对应的Adapter
        CommonAdapter listAdapter = (CommonAdapter) binding.lvTradingList.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, binding.lvTradingList);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = binding.lvTradingList.getLayoutParams();
        params.height = totalHeight+ (binding.lvTradingList.getDividerHeight() * (listAdapter.getCount()));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        binding.lvTradingList.setLayoutParams(params);
    }


}

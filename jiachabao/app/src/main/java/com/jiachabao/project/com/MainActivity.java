package com.jiachabao.project.com;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.load.engine.Resource;
import com.jiachabao.project.com.component.view.NoScrollViewPager;
import com.jiachabao.project.com.databinding.ActivityMainBinding;
import com.jiachabao.project.com.home.HomeFragment;
import com.jiachabao.project.com.trading.TradingFragment;
import com.jiachabao.project.com.user.UserFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private HomeFragment homeFragment;
    private TradingFragment tradingFragment;
    private UserFragment userFragment;
    private ArrayList<Fragment> fragments;
    private String menuName="home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        initView();
    }


    private void initView(){
        fragments=new ArrayList<Fragment>();
        homeFragment=new HomeFragment();
        tradingFragment=new TradingFragment();
        userFragment=new UserFragment();

        fragments.add(homeFragment);
        fragments.add(tradingFragment);
        fragments.add(userFragment);
        binding.vpContent.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        binding.vpContent.setNoScroll(true);
        binding.vpContent.setOffscreenPageLimit(fragments.size());

        binding.ivTabHome.setSelected(true);
        binding.tvTabHome.setTextColor(Color.parseColor("#d1ad61"));
        binding.flTabHome.setOnClickListener(this);
        binding.flTabTrading.setOnClickListener(this);
        binding.flTabMy.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fl_tab_home:
                if(menuName.equals("home")){
                    return;
                }
                binding.ivTabHome.setSelected(true);
                binding.tvTabHome.setTextColor(Color.parseColor("#d1ad61"));
                binding.ivTabTrading.setSelected(false);
                binding.ivTabMy.setSelected(false);
                binding.tvTabTrading.setTextColor(Color.parseColor("#333333"));
                binding.tvTabMy.setTextColor(Color.parseColor("#333333"));
                binding.vpContent.setCurrentItem(0,false);
                menuName="home";
                break;
            case R.id.fl_tab_trading:
                if(menuName.equals("trading")){
                    return;
                }
                binding.ivTabHome.setSelected(false);
                binding.ivTabTrading.setSelected(true);
                binding.ivTabMy.setSelected(false);
                binding.tvTabHome.setTextColor(Color.parseColor("#333333"));
                binding.tvTabTrading.setTextColor(Color.parseColor("#d1ad61"));
                binding.tvTabMy.setTextColor(Color.parseColor("#333333"));
                binding.vpContent.setCurrentItem(1,false);
                menuName="trading";
                break;
            case R.id.fl_tab_my:
                if(menuName.equals("user")){
                    return;
                }
                binding.ivTabHome.setSelected(false);
                binding.ivTabTrading.setSelected(false);
                binding.ivTabMy.setSelected(true);
                binding.tvTabHome.setTextColor(Color.parseColor("#333333"));
                binding.tvTabTrading.setTextColor(Color.parseColor("#333333"));
                binding.tvTabMy.setTextColor(Color.parseColor("#d1ad61"));
                binding.vpContent.setCurrentItem(2,false);
                menuName="user";
                break;
        }
    }




}

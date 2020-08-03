package com.lvchuan.ad;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.lvchuan.ad.base.BaseActivity;
import com.lvchuan.ad.view.adapter.MyFragmentAdapter;
import com.lvchuan.ad.view.fragment.AdFragment;
import com.lvchuan.ad.view.fragment.StatisticsFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FirstActivity extends BaseActivity {


    private ViewPager viewPager;



    @Override
    public int getLayoutRes() {
        return R.layout.activity_first;
    }

    @Override
    public void initView() {
        viewPager = findViewById(R.id.view_pager);
        initViewPager();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v, int id) {

    }



    private void initViewPager() {
        StatisticsFragment statisticsFragment= new StatisticsFragment();
        AdFragment adFragment= new AdFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(statisticsFragment);
        fragments.add(adFragment);
        MyFragmentAdapter  myFragmentAdapter = new MyFragmentAdapter(
                getSupportFragmentManager(), fragments);

        viewPager.setAdapter(myFragmentAdapter);



        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    viewPager.setCurrentItem(1, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule(task,10000,10000);
    }
}

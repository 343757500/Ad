package com.lvchuan.ad;

import android.app.Activity;
import android.graphics.Color;
import android.media.session.MediaController;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lvchuan.ad.view.adapter.DailyRecoveryAdapter;
import com.lvchuan.ad.view.adapter.DailyRecoveryNullAdapter;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DemoActivity extends Activity {

    private StandardGSYVideoPlayer svp;
    private RecyclerView rv;
    private List<String> list = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏

        setContentView(R.layout.activity_demo);


        list.add("纸类");
        list.add("金属/塑料");
        list.add("饮料瓶");
        list.add("织物类");

        TextView time1 = findViewById(R.id.time1);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        DailyRecoveryAdapter dailyRecoveryAdapter = new DailyRecoveryAdapter(this, null);
        rv.setAdapter(dailyRecoveryAdapter);
        dailyRecoveryAdapter.setDatas(list);

  /*      RecyclerView rv1 = findViewById(R.id.rv1);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        DailyRecoveryNullAdapter dailyRecoveryAdapter2 = new DailyRecoveryNullAdapter(this, null);
        rv1.setAdapter(dailyRecoveryAdapter2);
        dailyRecoveryAdapter2.setDatas(list);*/


        AnimatedPieView mAnimatedPieView = findViewById(R.id.animatedPieView);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)// 起始角度偏移
                .addData(new SimplePieInfo(27.66, ContextCompat.getColor(this,R.color.bg1), "这是第一段"))//数据（实现IPieInfo接口的bean）
                .addData(new SimplePieInfo(7.53, ContextCompat.getColor(this,R.color.bg2), "这是第二段"))
                .addData(new SimplePieInfo(24.36,ContextCompat.getColor(this,R.color.bg3), "这是第二段"))
                .addData(new SimplePieInfo(40.45, ContextCompat.getColor(this,R.color.bg4), "这是第二段"))
                // ...(尽管addData吧)
                .duration(2000)
                .strokeMode(false)
                .drawText(true);// 持续时间

// 以下两句可以直接用 mAnimatedPieView.start(config); 解决，功能一致
        mAnimatedPieView.applyConfig(config);
        mAnimatedPieView.start();



        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat  format=new SimpleDateFormat("EEEE");
        time1.setText(simpleDateFormat.format(date)+"  "+format.format(date));

    }
}

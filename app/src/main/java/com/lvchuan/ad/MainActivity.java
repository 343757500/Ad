package com.lvchuan.ad;

import androidx.appcompat.app.AppCompatActivity;
/*import androidx.recyclerview.widget.LinearLayoutManager;*/

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.just.agentweb.AgentWeb;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.lvchuan.ad.bean.AdEntity;
import com.lzy.okgo.OkGo;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , OnItemClickListener  {

    private AgentWeb agentWeb;
    private LinearLayout lin_web;
    private ConvenientBanner adBanner;
    List<AdEntity> bannerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        lin_web = findViewById(R.id.lin_web);
        adBanner = findViewById(R.id.adBanner);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(lin_web, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()//进度条
                .createAgentWeb()
                .ready()
                .go("https://120.78.175.246/apps/#/?boxCode=LZ02-664342");

        AdEntity adEntity1 = new AdEntity();
        adEntity1.setImgHref("https://dummyimage.com/600x400/00ff00/0011ff.png&text=HelloWorld");
        adEntity1.setId(0);
        AdEntity adEntity2 = new AdEntity();
        adEntity2.setId(1);
        adEntity2.setImgHref("https://stream7.iqilu.com/10339/upload_transcode/202002/17/20200217101826WjyFCbUXQ2.mp4");
        AdEntity adEntity = new AdEntity();
        adEntity.setId(2);
        adEntity.setImgHref("http://mmbiz.qpic.cn/mmbiz/PwIlO51l7wuFyoFwAXfqPNETWCibjNACIt6ydN7vw8LeIwT7IjyG3eeribmK4rhibecvNKiaT2qeJRIWXLuKYPiaqtQ/0");
        bannerList.add(adEntity1);
        bannerList.add(adEntity2);
        bannerList.add(adEntity);


        adBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
                LocalVImageHolderView localImageHolderView = new LocalVImageHolderView(itemView);
                localImageHolderView.setCallback((curId, time) -> {
                    handleAdTurn(curId, time);
                });
                return localImageHolderView;
            }

            @Override
            public int getLayoutId() {
                return R.layout.banner_item;
            }
        }, bannerList) .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPointViewVisible(true).setCanLoop(true).setOnItemClickListener(this);


        initData();

    }

    private void handleAdTurn(int curId, long time) {
        Log.e("MainActivity",curId+"::"+time);
        if (adBanner != null) {
            adBanner.postDelayed(() -> {
                if (curId < bannerList.size() - 1) {
                    if (adBanner != null) adBanner.setCurrentItem(curId + 1, false);
                } else {
                    if (adBanner != null) adBanner.notifyDataSetChanged();
                }
            }, time);
        }
    }


    private void initData() {
       // OkGo.get()

        initDownLoad();

    }

    private void initDownLoad() {
        FileDownloader.getImpl().create("https://dummyimage.com/600x400/00ff00/0011ff.png&text=HelloWorld").setWifiRequired(true).setPath("a.png").setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                Log.e("FileDownloader",totalBytes+"");
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                int percent=(int) ((double) soFarBytes / (double) totalBytes * 100);
               // textView.setText("("+percent+"%"+")");
            }

            @Override
            protected void blockComplete(BaseDownloadTask task) {
                Log.e("FileDownloader",task.getEtag()+"");
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                Toast.makeText(MainActivity.this,"下载完成!",Toast.LENGTH_SHORT).show();
                //textView.setText("("+"100%"+")");
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                Log.e("FileDownloader",totalBytes+"");
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                Log.e("FileDownloader",e.getMessage());
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                //continueDownLoad(task);//如果存在了相同的任务，那么就继续下载
                Log.e("FileDownloader","");
            }
        }).start();
    }


    //netty接收到广告更换的广播
    @Subscriber(tag = "handlecmd")
    public void handlecmd(String message) {
        //JSONObject jsonObject = JSONObject.parseObject(message);

        if (message.equals("0")){
            lin_web.setVisibility(View.GONE);
            adBanner.setVisibility(View.VISIBLE);

        }else if (message.equals("1")){
            adBanner.setVisibility(View.GONE);
            lin_web.setVisibility(View.VISIBLE);
            agentWeb = AgentWeb.with(this)
                    .setAgentWebParent(lin_web, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()//进度条
                    .createAgentWeb()
                    .ready()
                    .go("http://120.78.175.246/apps/#/?boxCode=LZ02-584300");

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_1:
                EventBus.getDefault().post("0","handlecmd");
                break;
            case R.id.bt_2:
                EventBus.getDefault().post("1","handlecmd");
                break;
        }
    }

    @Override
    public void onItemClick(int position) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        //开始执行轮播，并设置轮播时长
        //  adBanner.startTurning(10000);

    }

    @Override
    protected void onPause() {
        super.onPause();
       // adBanner.stopTurning();
    }
}
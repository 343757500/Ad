package com.lvchuan.ad.view.fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lvchuan.ad.R;
import com.lvchuan.ad.base.BaseFragment;
import com.lvchuan.ad.bean.NettyCmdBean;
import com.lvchuan.ad.bean.StatisticsBean;
import com.lvchuan.ad.utils.SharedPreUtil;
import com.lvchuan.ad.view.adapter.DailyColorAdapter;
import com.lvchuan.ad.view.adapter.DailyRecoveryAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.simple.eventbus.Subscriber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class StatisticsFragment extends BaseFragment {
    private RecyclerView rv;
    private DailyRecoveryAdapter dailyRecoveryAdapter;
    private AnimatedPieViewConfig config;
    private AnimatedPieView mAnimatedPieView;
    private int [] colorList ={R.color.bg1,R.color.bg2,R.color.bg2,R.color.bg3};
    //private DailyColorAdapter dailyColorAdapter;
   private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 0:
                    List<StatisticsBean.ReturnInfoBean.DataBean> data=(List<StatisticsBean.ReturnInfoBean.DataBean>)msg.obj;
                    dailyRecoveryAdapter.setDatas(data);
                    break;
                case 1:
                    mAnimatedPieView.applyConfig(config);
                    mAnimatedPieView.start();
                    break;
            }
        };
    };

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_statistics;
    }

    @Override
    public void initView() {

        TextView time1 = findView(R.id.time1);
        rv = findView(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        dailyRecoveryAdapter = new DailyRecoveryAdapter(getActivity(), null);
        rv.setAdapter(dailyRecoveryAdapter);
       /* RecyclerView rv_color = findView(R.id.rv_color);
        dailyColorAdapter = new DailyColorAdapter(getActivity(), null);
        rv_color.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        rv_color.setAdapter(dailyColorAdapter);*/

  /*      RecyclerView rv1 = findViewById(R.id.rv1);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        DailyRecoveryNullAdapter dailyRecoveryAdapter2 = new DailyRecoveryNullAdapter(this, null);
        rv1.setAdapter(dailyRecoveryAdapter2);
        dailyRecoveryAdapter2.setDatas(list);*/


        mAnimatedPieView = findView(R.id.animatedPieView);
        config = new AnimatedPieViewConfig();




        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat  format=new SimpleDateFormat("EEEE");
        time1.setText(simpleDateFormat.format(date)+"  "+format.format(date));
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        OkGo.<String>get("http://192.168.11.130:8824/mobile/advertisementMonitor/znRecycleBoxAnalysis")
                .params("time","thisMonth")
                .params("devId", SharedPreUtil.getString(getActivity(),"devId",""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("StatisticsFragment",response.body());
                        StatisticsBean statisticsBean = new Gson().fromJson(response.body().toString(), StatisticsBean.class);
                        List<StatisticsBean.ReturnInfoBean> returnList = statisticsBean.getReturn_info();
                        List<StatisticsBean.ReturnInfoBean.DataBean> data = returnList.get(0).getData();


                        Message message = Message.obtain();
                        message.what = 0;
                        message.obj=data;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("StatisticsFragment","");
                    }
                });


        OkGo.<String>get("http://192.168.11.130:8824/mobile/advertisementMonitor/znRecycleBoxAnalysis")
                .params("time","thisMonth")
                .params("devId", SharedPreUtil.getString(getActivity(),"devId",""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("StatisticsFragment",response.body());
                        StatisticsBean statisticsBean = new Gson().fromJson(response.body().toString(), StatisticsBean.class);
                        List<StatisticsBean.ReturnInfoBean> returnList = statisticsBean.getReturn_info();
                        List<StatisticsBean.ReturnInfoBean.DataBean> data = returnList.get(0).getData();

                        for (int i = 0; i < data.size(); i++) {
                            config.startAngle(-90)// 起始角度偏移
                                    .addData(new SimplePieInfo(Integer.parseInt(data.get(i).getRecycleType()), ContextCompat.getColor(getActivity(),colorList[i]), data.get(i).getRecycleName()))//数据（实现IPieInfo接口的bean）
                                    .duration(2000)
                                    .setTextSize(50)
                                    .strokeMode(false)
                                    .pieRadius(300)
                                    .drawText(true);// 持续时间
                        }


// 以下两句可以直接用 mAnimatedPieView.start(config); 解决，功能一致

                        Message message = Message.obtain();
                        message.what = 1;
                        mHandler.sendMessage(message);
                        //dailyColorAdapter.setDatas(data);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });

    }





    //netty接收到广告更换的广播
    @Subscriber(tag = "nettyCmdBean")
    private void nettyCmdBean(NettyCmdBean nettyCmdBean) {

    }

    @Override
    public void onClick(View v, int id) {

    }
}

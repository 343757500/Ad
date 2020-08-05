package com.lvchuan.ad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.lvchuan.ad.base.BaseActivity;
import com.lvchuan.ad.netty.Clients;
import com.lvchuan.ad.service.LoopService;
import com.lvchuan.ad.utils.SharedPreUtil;
import com.lvchuan.ad.view.adapter.MyFragmentAdapter;
import com.lvchuan.ad.view.fragment.AdFragment;
import com.lvchuan.ad.view.fragment.ConfigInputFragment;
import com.lvchuan.ad.view.fragment.StatisticsFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxDeviceTool;

import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import static android.content.ContentValues.TAG;

public class FirstActivity extends BaseActivity {


    private final Timer timer = new Timer();
    private ViewPager viewPager;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            viewPager.setCurrentItem(1, false);

            super.handleMessage(msg);
        }
    };
    private TextView tv_config_or_get_ad;
    private LinearLayout ll_setting;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_first;
    }

    @Override
    public void initView() {
        viewPager = findViewById(R.id.view_pager);
        tv_config_or_get_ad = findViewById(R.id.tv_config_or_get_ad);
        ll_setting = findViewById(R.id.ll_setting);
        initViewPager();

        //启动netty服务
       // Clients.getInstance().start();

        applyRxPermissions();


        //获取系统的LocationManager对象
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //添加权限检查
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //设置每一秒获取一次location信息
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,      //GPS定位提供者
                1000,       //更新数据时间为1秒
                1,      //位置间隔为1米
                //位置监听器
                new LocationListener() {  //GPS定位信息发生改变时触发，用于更新位置信息

                    @Override
                    public void onLocationChanged(Location location) {
                        //GPS信息发生改变时，更新位置
                        locationUpdates(location);
                    }

                    @Override
                    //位置状态发生改变时触发
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    //定位提供者启动时触发
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    //定位提供者关闭时触发
                    public void onProviderDisabled(String provider) {
                    }
                });
        //从GPS获取最新的定位信息
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationUpdates(location);    //将最新的定位信息传递给创建的locationUpdates()方法中
    }






    @SuppressLint("CheckResult")
    private void applyRxPermissions() {
        Log.i("Permissions", "applyRxPermissions");
        //获取app的DEVID唯一设备号
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        //激活人脸
                        Log.e("111","获得权限成功");
                        String devId = RxDeviceTool.getDeviceIdIMEI(this);
                        Log.e("devId",devId);
                    } else {
                        Toast.makeText(this, "没有获得权限", Toast.LENGTH_LONG).show();
                    }
                });
    }









    public void locationUpdates(Location location) {  //获取指定的查询信息
        //如果location不为空时
        if (location != null) {
            StringBuilder stringBuilder = new StringBuilder();        //使用StringBuilder保存数据
            //获取经度、纬度、等属性值
            stringBuilder.append("您的位置信息：\n");
            stringBuilder.append("经度：");
            stringBuilder.append(location.getLongitude());
            stringBuilder.append("\n纬度：");
            stringBuilder.append(location.getLatitude());
            Log.e("111",stringBuilder.toString());
//            stringBuilder.append("\n精确度：");
//            stringBuilder.append(location.getAccuracy());
//            stringBuilder.append("\n高度：");
//            stringBuilder.append(location.getAltitude());
//            stringBuilder.append("\n方向：");
//            stringBuilder.append(location.getBearing());
//            stringBuilder.append("\n速度：");
//            stringBuilder.append(location.getSpeed());
//            stringBuilder.append("\n时间：");
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH mm ss");    //设置日期时间格式
//            stringBuilder.append(dateFormat.format(new Date(location.getTime())));
        } else {
            //否则输出空信息
            Log.e("111","没有获取到GPS信息");
            //无法定位：1、提示用户打开定位服务；2、跳转到设置界面
            Toast.makeText(this, "无法定位，请打开定位服务", Toast.LENGTH_SHORT).show();
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }


    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v, int id) {
        if (id==R.id.tv_config_or_get_ad){
            ConfigInputFragment.showDialog(this, (config) -> {
               // getAdByConfig(config, true);
                if (config.equals("123456&1")){
                    viewPager.setVisibility(View.VISIBLE);
                    ll_setting.setVisibility(View.GONE);
                }
            });
        }
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




        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 20000, 20000);
    }






    @Subscriber(tag = "startService")
    private void setService(String staues) {

        if ("1".equals(staues)){
            Intent intentFour = new Intent(this, LoopService.class);
            startService(intentFour);
        }else{
            Intent intentFour = new Intent(this, LoopService.class);
            stopService(intentFour);
        }

    }



    @Subscriber(tag = "devId")
    private void postDevId(String devIds) {
        try {
            String devId= SharedPreUtil.getString(FirstActivity.this,"devId","");
            if (!"".equals(devId)) {
                Channel channel = Clients.channel;
                TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame("{\"flag\":\"BindChannelAndDevice\",\"devId\":\"" + devId + "\"}");
                channel.writeAndFlush(textWebSocketFrame);
            }
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

    }




}

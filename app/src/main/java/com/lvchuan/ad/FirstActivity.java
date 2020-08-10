package com.lvchuan.ad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.lvchuan.ad.base.BaseActivity;
import com.lvchuan.ad.bean.NettyCmdBean;
import com.lvchuan.ad.netty.Clients;
import com.lvchuan.ad.service.LoopService;
import com.lvchuan.ad.utils.SharedPreUtil;
import com.lvchuan.ad.view.adapter.MyFragmentAdapter;
import com.lvchuan.ad.view.fragment.AdFragment;
import com.lvchuan.ad.view.fragment.ConfigInputFragment;
import com.lvchuan.ad.view.fragment.H5ViewFragment;
import com.lvchuan.ad.view.fragment.StatisticsFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxDeviceTool;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
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

            viewPager.setCurrentItem(2, false);

            super.handleMessage(msg);
        }
    };
    private TextView tv_config_or_get_ad;
    private LinearLayout ll_setting;
    private String locationProvider;
    private LocationManager locationManager;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_first;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        viewPager = findViewById(R.id.view_pager);
        tv_config_or_get_ad = findViewById(R.id.tv_config_or_get_ad);
        ll_setting = findViewById(R.id.ll_setting);
        initViewPager();
        //启动netty服务
        Clients.getInstance().start();
        applyRxPermissions();
        //getGps();
    }


    private void getGps() {
        getLocation();
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
                        SharedPreUtil.saveString(this,"devId",devId);
                        Log.e("devId",devId);
                    } else {
                        Toast.makeText(this, "没有获得权限", Toast.LENGTH_LONG).show();
                    }
                });
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
        H5ViewFragment h5ViewFragment = new H5ViewFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(statisticsFragment);
        fragments.add(adFragment);
        fragments.add(h5ViewFragment);
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



    @Subscriber(tag = "nettyCmdBean")
    private void nettyCmdBean(NettyCmdBean nettyCmdBean) {
        //viewPager.setCurrentItem(Integer.parseInt(nettyCmdBean.getMode()), false);
    }













    public static final int LOCATION_CODE = 301;
    private void getLocation () {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
            } else {
                //监视地理位置变化
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
                Location location = locationManager.getLastKnownLocation(locationProvider);
                Log.e("111","---"+location);
                if (location != null) {
                    //输入经纬度
                    Toast.makeText(this, location.getLongitude() + " " + location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            //监视地理位置变化
            locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                //不为空,显示地理位置经纬度
                Toast.makeText(this, location.getLongitude() + " " + location.getLatitude() + "", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
        }
        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
        }
        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //不为空,显示地理位置经纬度
                Toast.makeText(FirstActivity.this, location.getLongitude() + " " + location.getLatitude() + "", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "申请权限", Toast.LENGTH_LONG).show();
                    try {
                        List<String> providers = locationManager.getProviders(true);
                        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                            //如果是Network
                            locationProvider = LocationManager.NETWORK_PROVIDER;
                        }else if (providers.contains(LocationManager.GPS_PROVIDER)) {
                            //如果是GPS
                            locationProvider = LocationManager.GPS_PROVIDER;
                        }
                        //监视地理位置变化
                        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
                        Location location = locationManager.getLastKnownLocation(locationProvider);
                        if (location != null) {
                            //不为空,显示地理位置经纬度
                            Toast.makeText(this, location.getLongitude() + " " + location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                        }
                    }catch (SecurityException e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "缺少权限", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

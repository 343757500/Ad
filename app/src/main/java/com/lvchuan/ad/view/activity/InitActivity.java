package com.lvchuan.ad.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lvchuan.ad.R;
import com.lvchuan.ad.base.BaseActivity;
import com.lvchuan.ad.netty.Clients;
import com.lvchuan.ad.service.UpPictureService;
import com.lvchuan.ad.utils.SharedPreUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxDeviceTool;

import io.netty.util.internal.StringUtil;

public class InitActivity extends BaseActivity {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_init;
    }

    @Override
    public void initView() {
       // applyRxPermissions();
        String devId = RxDeviceTool.getDeviceIdIMEI(this);
        if (StringUtil.isNullOrEmpty(devId)){
            Intent intent = new Intent(this,FirstActivity.class);
            this.startActivity(intent);
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
                        Log.e("111","获得权限成功");
                        String devId = RxDeviceTool.getDeviceIdIMEI(this);
                        SharedPreUtil.saveString(this,"devId",devId);
                        Intent intent = new Intent(this,FirstActivity.class);
                        this.startActivity(intent);
                    } else {
                        Toast.makeText(this, "没有获得权限", Toast.LENGTH_LONG).show();
                    }
                });
    }
}

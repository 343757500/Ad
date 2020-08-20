package com.lvchuan.ad.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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

import com.google.gson.Gson;
import com.lvchuan.ad.R;
import com.lvchuan.ad.base.BaseActivity;
import com.lvchuan.ad.bean.EorrBean;
import com.lvchuan.ad.bean.InitBean;
import com.lvchuan.ad.bean.NettyCmdBean;
import com.lvchuan.ad.bean.UpdateBean;
import com.lvchuan.ad.http.BaseUrl;
import com.lvchuan.ad.http.HttpUrl;
import com.lvchuan.ad.netty.Clients;
import com.lvchuan.ad.service.LoopService;
import com.lvchuan.ad.service.UpPictureService;
import com.lvchuan.ad.transformer.DepthPageTransformer;
import com.lvchuan.ad.transformer.ZoomOutPageTransformer;
import com.lvchuan.ad.utils.OkGoUpdateHttpUtil;
import com.lvchuan.ad.utils.PackageUtils;
import com.lvchuan.ad.utils.SharedPreUtil;
import com.lvchuan.ad.utils.ShellUtils;
import com.lvchuan.ad.view.adapter.MyFragmentAdapter;
import com.lvchuan.ad.view.fragment.AdFragment;
import com.lvchuan.ad.view.fragment.ConfigInputFragment;
import com.lvchuan.ad.view.fragment.H5ViewFragment;
import com.lvchuan.ad.view.fragment.StatisticsFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.service.DownloadService;
import com.vondear.rxtool.RxAppTool;
import com.vondear.rxtool.RxDeviceTool;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import static android.content.ContentValues.TAG;
import static com.vondear.rxtool.view.RxToast.showToast;

public class FirstActivity extends BaseActivity {


    private final Timer timer = new Timer();
    private ViewPager viewPager;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //viewPager.setCurrentItem(1, false);

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
        hideBottomUIMenu();
        viewPager = findViewById(R.id.view_pager);
        tv_config_or_get_ad = findViewById(R.id.tv_config_or_get_ad);
        ll_setting = findViewById(R.id.ll_setting);
        initViewPager();


        getGps();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyRxPermissions();
    }

    private void initRegister() {
        String sendId = BaseUrl.BASE_URL + HttpUrl.REGISTER;
        String devId = SharedPreUtil.getString(FirstActivity.this, "devId", "");
        OkGo.<String>get(sendId)
                .params("devId", devId)
                .params("longitude", "0")
                .params("latitude", "0")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        sendDevId(devId);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }


    private void getGps() {

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
                        Log.e("111", "获得权限成功");
                        String devId = RxDeviceTool.getDeviceIdIMEI(this);
                        SharedPreUtil.saveString(this, "devId", devId);

                        //启动netty服务
                        Clients.getInstance().start();
                        //注册设备
                        initRegister();
                        //检测是否需要更新apk
                        checkVersion();
                        Log.e("devId", devId);

                     /*   Intent intentFour = new Intent(this, UpPictureService.class);
                        startService(intentFour);*/

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
        if (id == R.id.tv_config_or_get_ad) {
            ConfigInputFragment.showDialog(this, (config) -> {
                // getAdByConfig(config, true);
                if (config.equals("123456&1")) {
                    viewPager.setVisibility(View.VISIBLE);
                    ll_setting.setVisibility(View.GONE);
                }
            });
        }
    }


    private void initViewPager() {
        AdFragment adFragment = new AdFragment();
        StatisticsFragment statisticsFragment = new StatisticsFragment();
        H5ViewFragment h5ViewFragment = new H5ViewFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(adFragment);
        fragments.add(statisticsFragment);
        fragments.add(h5ViewFragment);
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(
                getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myFragmentAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setOffscreenPageLimit(fragments.size()); //预加载
        viewPager.setCurrentItem(0, true);

    }


    @Override
    protected void onResume() {
        super.onResume();
        String devId = SharedPreUtil.getString(FirstActivity.this, "devId", "");
        if (!"".equals(devId)) {
            sendDevId(SharedPreUtil.getString(FirstActivity.this, "devId", ""));
        }
    }

    // 发送设备Id到服务器
    private void sendDevId(String devId) {
        String sendId = BaseUrl.BASE_URL + HttpUrl.INIT;
        OkGo.<String>get(sendId)
                .params("devId", devId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String initJson = response.body().toString();
                            if (initJson.contains("true")) {
                                InitBean initBean = new Gson().fromJson(initJson, InitBean.class);
                                String mode = initBean.getReturn_info().get(0).getMode();
                                viewPager.setCurrentItem(Integer.parseInt(mode) - 1, true);
                                Log.e("FirstActivity", "发送广播" + new Gson().toJson(initBean));
                                EventBus.getDefault().post(initBean, "initBean");
                            } else {
                                viewPager.setCurrentItem(0, true);
                                EventBus.getDefault().post("", "initNotBean");
                                EorrBean eorrBean = new Gson().fromJson(initJson, EorrBean.class);
                                Toast.makeText(FirstActivity.this, eorrBean.getCommon_return() + "", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("FirstActivity", e.getMessage());
                            Toast.makeText(FirstActivity.this, "系统异常", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }


    @Subscriber(tag = "startService")
    private void setService(String staues) {

        if ("1".equals(staues)) {
            boolean serviceRunning = isServiceRunning("com.lvchuan.ad.service.LoopService");
            if (serviceRunning) {
                return;
            }
            Intent intentFour = new Intent(this, LoopService.class);
            startService(intentFour);
        } else {
            Intent intentFour = new Intent(this, LoopService.class);
            stopService(intentFour);
        }

    }


    /**
     * 判断服务是否运行
     */
    private boolean isServiceRunning(final String className) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }


    @Subscriber(tag = "devId")
    private void postDevId(String devIds) {
        try {
            String devId = SharedPreUtil.getString(FirstActivity.this, "devId", "");
            if (!"".equals(devId)) {
                Channel channel = Clients.channel;
                TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame("{\"flag\":\"BindAndroidChannelAndDevice\",\"devId\":\"" + devId + "\"}");
                channel.writeAndFlush(textWebSocketFrame);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    @Subscriber(tag = "viewChange")
    private void viewChange(String devIds) {
        viewPager.setCurrentItem(0, true);
    }


    @Subscriber(tag = "nettyCmdBean")
    private void nettyCmdBean(NettyCmdBean nettyCmdBean) {
        Log.e("nettyCmdBean", "广播：：" + new Gson().toJson(nettyCmdBean));
        if ("init".equals(nettyCmdBean.getFlag())) {
            sendDevId(SharedPreUtil.getString(FirstActivity.this, "devId", ""));
        } else if ("update".equals(nettyCmdBean.getFlag())) {
            checkVersion();
        } else if ("restart".equals(nettyCmdBean.getFlag())) {
            Log.e("nettyCmdBean", "重启 广播：：" + new Gson().toJson(nettyCmdBean));
            restartApp();
        }
    }


    public void restartApp() {
        // 重启应用
        SystemClock.sleep(500);
        Intent intent = new Intent(this, FirstActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }




    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }



    private void checkVersion() {
        String appVersionName = RxAppTool.getAppVersionName(FirstActivity.this);
        String url = BaseUrl.BASE_URL + HttpUrl.UPDATE;
        OkGo.<String>get(url)
                .params("type","2")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String result = response.body();
                            // 版本需要更新
                            UpdateBean updateAppBean = new Gson().fromJson(result, UpdateBean.class);
                            Double netVersion  = Double.valueOf(updateAppBean.getData().getVersion());
                            Double localVersion  = Double.valueOf(RxAppTool.getAppVersionName(FirstActivity.this));
                            if (netVersion>localVersion){
                                downloadApk(updateAppBean.getData().getPath());
                            }else{
                                Log.e("downloadApk","无需版本更新");
                            }
                        }catch (Exception e){
                            Log.e("111",e.getMessage());
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }


    private void downloadApk(String downloadUrl) {
        UpdateAppBean updateAppBean = new UpdateAppBean();
        //设置 apk 的下载地址
        updateAppBean.setApkFileUrl(downloadUrl);
        //Apk本地的保存路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/360";
        //设置apk 的保存路径
        updateAppBean.setTargetPath(path);
        updateAppBean.setHttpManager(new OkGoUpdateHttpUtil());
        UpdateAppManager.download(this, updateAppBean, new DownloadService.DownloadCallback() {
            @Override
            public void onStart() {
                Log.e("downloadApk","正在更新版本中...");
                //HProgressDialogUtils.showHorizontalProgressDialog(HomeActivity.this, "正在更新版本中...", false);
            }

            @Override
            public void onProgress(float progress, long totalSize) {
                //HProgressDialogUtils.setProgress(Math.round(progress * 100));
                Log.e("downloadApk",Math.round(progress * 100)+"");
            }

            @Override
            public void setMax(long totalSize) {
            }

            @Override
            public boolean onFinish(File file) {
               // HProgressDialogUtils.cancel();
               // Log.d("安装地址", file.getPath());
                Log.e("downloadApk","安装地址"+file.getPath());
               // installerApp(file.getPath());
                boolean install = install(file.getPath());
                Toast.makeText(FirstActivity.this,"安装:"+install,Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public void onError(String msg) {
                //HProgressDialogUtils.cancel();
            }

            @Override
            public boolean onInstallAppAndAppOnForeground(File file) {
                return true;
            }
        });
    }

    // 静默安装
    private void installerApp(String apkPath) {
        boolean isRoot = ShellUtils.checkRootPermission();
        Log.e("downloadApk","设备是否root:"+isRoot);
        if (isRoot) {
            int resultCode = PackageUtils.installSilent(this, apkPath);
            if (resultCode != PackageUtils.INSTALL_SUCCEEDED) {
                Toast.makeText(this, "升级失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "升级成功", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public boolean install(String apkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
            }
        }
        return result;
    }

    //3:静默安装后需要自动启动,因此就需要注册安装替换静态广播,但是必须是7.0以下才行,因为8.0删除了该静态广播注册的功能
    public class OpenApkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent localIntent;
            if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
                localIntent = new Intent(context, FirstActivity.class);
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(localIntent);
            }
        }
    }

}

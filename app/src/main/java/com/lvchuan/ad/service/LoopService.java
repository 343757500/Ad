package com.lvchuan.ad.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;

import androidx.annotation.Nullable;

import com.lvchuan.ad.netty.Clients;

public class LoopService extends Service {
  public  static Handler handlerAlive=new Handler();
   public static Runnable runnableAlive=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            permissionAlive();
            handlerAlive.postDelayed(this, 20000);
        }
    };


    @Override
    public void onCreate() {

    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;

    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {
      // String wxno1 = intent.getStringExtra("wxno");

        handlerAlive.postDelayed(runnableAlive, 20000);//每两秒执行一次runnable.

        return START_REDELIVER_INTENT;
        /*    1 改动Service的onStartCommand方法中的返回值。onStartCommand方法有三种返回值，依次是
        START_NOT_STICK:
        当Service被异常杀死时。系统不会再去尝试再次启动这个Service
                START_STICKY
        当Service被异常杀死时。系统会再去尝试再次启动这个Service，可是之前的Intent会丢失，也就是在onStartCommand中接收到的Intent会是null
                START_REDELIVER_INTENT
        当Service被异常杀死时，系统会再去尝试再次启动这个Service。而且之前的Intent也会又一次被传给onStartCommand方法
        通过改动onStartCommand方法的返回值这一方法足以解决上面我们提到Service被关闭的第一种情况。 可是对于用户主动强制关闭和三方管理器还是没有效果的
        //return super.onStartCommand(intent, flags, startId);*/
    }


    //是否保活权限
    private static final void permissionAlive() {
        Log.e("LoopService","LoopService");
        Clients.getInstance().doConnect();
    }


    @Override
    public void onDestroy() {
        Log.e("LoopService","LoopService  --OnDestory");
        stopForeground(true);
        handlerAlive.removeCallbacks(runnableAlive);
        Intent intent = new Intent("restartService");
        sendBroadcast(intent);
    }


    }


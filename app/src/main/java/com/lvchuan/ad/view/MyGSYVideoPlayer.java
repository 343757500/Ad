package com.lvchuan.ad.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;


/**
 * 兼容的空View，目前用于 GSYVideoManager的设置
 * Created by shuyu on 2016/11/11.
 */

public abstract class MyGSYVideoPlayer extends MyGSYBaseVideoPlayer {


    public MyGSYVideoPlayer(@NonNull Context context) {
        super(context);
    }

    public MyGSYVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGSYVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyGSYVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    /*******************************下面方法为管理器和播放控件交互的方法****************************************/

    @Override
    public GSYVideoViewBridge getGSYVideoManager() {
        GSYVideoManager.instance().initContext(getContext().getApplicationContext());
        return GSYVideoManager.instance();
    }

    @Override
    protected boolean backFromFull(Context context) {
        return GSYVideoManager.backFromWindowFull(context);
    }

    @Override
    protected void releaseVideos() {
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    protected int getFullId() {
        return GSYVideoManager.FULLSCREEN_ID;
    }

    @Override
    protected int getSmallId() {
        return GSYVideoManager.SMALL_ID;
    }

}
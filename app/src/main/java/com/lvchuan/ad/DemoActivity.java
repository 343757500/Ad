package com.lvchuan.ad;

import android.app.Activity;
import android.media.session.MediaController;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.Nullable;

import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;




public class DemoActivity extends Activity {

    private StandardGSYVideoPlayer svp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        svp = findViewById(R.id.svp);
        svp.setUp("http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4", true, "测试视频");
        svp.setVideoAllCallBack(new VideoAllCallBack() {
            @Override
            public void onStartPrepared(String url, Object... objects) {

            }

            @Override
            public void onPrepared(String url, Object... objects) {

            }

            @Override
            public void onClickStartIcon(String url, Object... objects) {

            }

            @Override
            public void onClickStartError(String url, Object... objects) {

            }

            @Override
            public void onClickStop(String url, Object... objects) {

            }

            @Override
            public void onClickStopFullscreen(String url, Object... objects) {

            }

            @Override
            public void onClickResume(String url, Object... objects) {

            }

            @Override
            public void onClickResumeFullscreen(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbar(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbarFullscreen(String url, Object... objects) {

            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                Log.e("demo","onAutoComplete");
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {

            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {

            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onEnterSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekVolume(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekPosition(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekLight(String url, Object... objects) {

            }

            @Override
            public void onPlayError(String url, Object... objects) {

            }

            @Override
            public void onClickStartThumb(String url, Object... objects) {

            }

            @Override
            public void onClickBlank(String url, Object... objects) {

            }

            @Override
            public void onClickBlankFullscreen(String url, Object... objects) {
                Log.e("demo","onClickBlankFullscreen");
            }
        });
    }

}

package com.lvchuan.ad;

import android.app.Application;


import androidx.multidex.MultiDex;

import com.liulishuo.filedownloader.FileDownloader;
import com.vondear.rxtool.RxTool;

public class ClientApplication extends Application {

    public static final String TAG = ClientApplication.class.getSimpleName();
    static ClientApplication instance;

    public static ClientApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FileUtil.createDir(Constants.BASE_CACHE_DIR);
        // 主要是添加下面这句代码
        MultiDex.install(this);
        FileDownloader.setup(this);//注意作者已经不建议使用init方法
        RxTool.init(this);
    }
}

package com.lvchuan.ad;

import android.os.Environment;

import java.io.File;

public class Constants {
    /**
     * 调试开关
     */
    public static boolean IS_TEST = false;

    /**
     * 缓存文件夹
     */
    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String BASE_CACHE_DIR = SDCARD_DIR + "/SmartRecycling";
    public static String AISLE_LOCK_STYLES = BASE_CACHE_DIR + "/aisleConfig.txt";
    public static String CLIENT_CONFIG = BASE_CACHE_DIR + "/config.txt";
    public static String CLIENT_DEVICE_INFO = BASE_CACHE_DIR + "/deviceinfo.txt";
    public static String CLIENT_DELIVERY_TYPES = BASE_CACHE_DIR + "/deliverytypes.txt";
    public static String AD_CACHE_DIR = BASE_CACHE_DIR + "/ad.txt";
    public static String AD_CONFIG_CACHE_DIR = BASE_CACHE_DIR + "/ad_cofing.txt";
    public static String WELCOME_AUDIO_OGG = BASE_CACHE_DIR + "/welocome.OGG";
    public static String LOGIN_AUDIO_OGG = BASE_CACHE_DIR + "/login.OGG";
    public static final String VIDEO_CACHE_DIR = BASE_CACHE_DIR + "/Video";
    public static final String LOG_CACHE_DIR = BASE_CACHE_DIR + "/log";
    public static final String ACFACE_DIR = BASE_CACHE_DIR + "/acface";
    public static String DEFAULT_TEST_URL = VIDEO_CACHE_DIR + "/default.mp4";
    public static final String APK_CACHE_DIR = BASE_CACHE_DIR + "/apk";
    public static final String PHOTO_DIR = BASE_CACHE_DIR + "/img";
    public static final String AD_IMAGE_DIR = BASE_CACHE_DIR + "/ad_img";
    public static final String POC_CAMERA_SNAPSHOT_DIR = BASE_CACHE_DIR + "/snapshot";
    public static final String POC_CAMERA_DIR = BASE_CACHE_DIR + "/poc_video";
    public static final String FACE_FEATURES = Constants.ACFACE_DIR + File.separator + "register" + File.separator + "features";
    public static final String FACE_SYNC = FACE_FEATURES + File.separator + "/sync_record.txt";
    public static final String monitoringIp[] = {"192.168.2.112", "192.168.2.113", "192.168.2.114", "192.168.2.115",
            "192.168.2.116", "192.168.2.117", "192.168.2.118", "192.168.2.119", "192.168.2.120"
    };
    /**
     * 对应以上ip的摄像头状态
     */
    public static boolean carmeraState[] = {false, false, false, false, false, false, false, false, false};

    /**
     * http协议头
     */
    public static final String PROTOCOL_HEADER = "https:";

    /**
     * 正式环境
     */
    public static final String HOST = "//cloud.youyiyun.tech/";
    public static final String HXNH_HOST = "//hxnh.top/recycling/";

    public static final String ad_small_suffix = "?x-oss-process=image/resize,w_1080,h_1920,m_fill/auto-orient,1/quality,q_70";
    public static final String bottom_small_suffix = "?x-oss-process=image/resize,w_1080,h_768,m_fill/auto-orient,1/quality,q_70";
    public static final String small_suffix = "?x-oss-process=image/resize,w_200,h_200,m_fill/auto-orient,1/quality,q_70";


}

package com.lvchuan.ad.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import android.util.Log;
 
/**
 * 截图工具类
 * @author Jzhung
 * @date 2016-7-13 17:25:18
 */
public class ScreenCaptureUtil {
    private static ScreenCaptureUtil instance;
    private byte[] tempBuffer;
    private StringBuilder buffer;
     
    private ScreenCaptureUtil(){
    }
     
    public static ScreenCaptureUtil getInstance(){
        if(instance == null){
            instance = new ScreenCaptureUtil();
            instance.tempBuffer = new byte[100 * 1024];
            instance.buffer = new StringBuilder(100 * 1024);
        }
        return instance;
    }
     
    /**
     * 调用系统截图工具获取屏幕截图字节数组，格式为png，注意这是一个耗时操作，约为1-5秒。
     * 如果屏幕分辨率过高，防止内存溢出，可以考虑直接保存到文件的命令u -c /system/bin/screencap -p /sdcard/screenshot.png
     * @return png格式图片的字节数组
     */
    public byte[] getScreenCapBytes(){
        //LogUtils.i("获取屏幕图片bytes");

        try {
            Process exec = Runtime.getRuntime().exec("su -c /system/bin/screencap -p");
            try {
                final InputStream inputStream = exec.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                //清空缓存内容
                buffer.setLength(0);
                int count;
                long start = System.currentTimeMillis();
                while ((count = bufferedInputStream.read(tempBuffer)) > 0) {
                    //Log.e("test", "count = " + count);
                    buffer.append(new String(tempBuffer, 0, count, "ISO-8859-1"));
                }
                Log.v("test", "获取截屏耗时:" +(System.currentTimeMillis() - start) + "ms png图片大小: " + buffer.length());
                bufferedInputStream.close();
                //Log.e("test", "result: " + buffer.toString());
                //exec.getOutputStream().close();
                final int retCode = exec.waitFor();
                exec.destroy();
                //Log.e("test", "retCode: " + retCode);
            } catch (final IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            tempBuffer =  buffer.toString().getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return tempBuffer;
    } 
}
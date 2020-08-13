package com.lvchuan.ad.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

public class NetworkUtil {
    public static final int NETWORKTYPE_4G = 0;
    public static final int NETWORKTYPE_2G = 1;
    public static final int NETWORKTYPE_3G = 2;
    public static final int NETWORKTYPE_NONE = 3;
    public static final int NETWORKTYPE_WIFI = 4;
    public static final int NETWORKTYPE_NOSIM = 5;
    public static final int ETHERNET = 6;

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.i("NET", "网络恢复");
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Gps是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = locationManager.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    /**
     * wifi是否打开
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断当前网络是否是wifi网络
     * if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE) {
     *
     * @param context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前网络是否3G网络
     *
     * @param context
     * @return boolean
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    public static int getNetWorkType(Context context, TelephonyManager mTelephonyManager) {
        int mNetWorkType = -1;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                return NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return isFastMobileNetwork(mTelephonyManager);
            }else if(type.equalsIgnoreCase("Ethernet")){
                return ETHERNET;
            }
        } else {
            if (!hasSimCard(mTelephonyManager)) {
                return NETWORKTYPE_NOSIM;
            } else {
                return NETWORKTYPE_4G;
            }
        }
        return mNetWorkType;
    }

    /**
     * 判断网络类型
     *
     * @return 0为4g，1为3g，2为2g
     */
    public static int isFastMobileNetwork(TelephonyManager mTelephonyManager) {
        if (mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
            return 0;
        } else if (mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
                mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
                mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
                mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static boolean hasSimCard(TelephonyManager mTelephonyManager) {
        int simState = mTelephonyManager.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }

    public static int ausToLevel(int aus) {
        if (aus >= 103) {
            return 1;
        } else if (aus > 83 && aus < 93) {
            return 2;
        } else if (aus > 73 && aus < 83) {
            return 3;
        } else if (aus > 63 && aus < 73) {
            return 4;
        } else if (aus < 63) {
            return 5;
        }
        return 1;
    }

    //这段是转换成点分式IP的码
    private static String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
    }
}

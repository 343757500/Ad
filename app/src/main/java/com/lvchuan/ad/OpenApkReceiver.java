package com.lvchuan.ad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OpenApkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent localIntent;
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            localIntent = new Intent(context, MainActivity.class);
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(localIntent);
        }
    }
}
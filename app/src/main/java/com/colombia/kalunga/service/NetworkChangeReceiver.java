package com.colombia.kalunga.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetooth = connMgr.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        Log.e("Network Available ", "OK");
        boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() ||
                mobile != null && mobile.isConnectedOrConnecting();
        Intent i = new Intent(context, StreamService.class);
        if (wifi.isConnected()) {
            i.putExtra("action", "pause");
            context.startService(i);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    i.putExtra("action", "play");
                    context.startService(i);
                    //the current activity will get finished.
                }
            }, 5000);
            Log.e("Network Available ", "wifi");
        } else if (mobile.isConnected()) {
            i.putExtra("action", "pause");
            context.startService(i);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    i.putExtra("action", "play");
                    context.startService(i);
                    //the current activity will get finished.
                }
            }, 5000);
            Log.e("Network Available ", "mobile");
        } else if (bluetooth.isConnected()) {
            i.putExtra("action", "pause");
            context.startService(i);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    i.putExtra("action", "play");
                    context.startService(i);
                    //the current activity will get finished.
                }
            }, 5000);
            Log.e("Network Available ", "bluetooth");
        } else {
            Log.e("Network Available ", "No");
        }
    }
}

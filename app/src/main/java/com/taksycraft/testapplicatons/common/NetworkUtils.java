package com.taksycraft.testapplicatons.common;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;

public class NetworkUtils {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public static boolean isInternetAvailable() {
        try {
            //do not run on mainthread
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}

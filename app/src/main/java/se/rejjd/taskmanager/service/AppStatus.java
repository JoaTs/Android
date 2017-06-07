package se.rejjd.taskmanager.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public final class AppStatus {

    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();

        } catch (Exception e) {
            Log.e("", "CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return false;
    }
}
package br.edu.ifpe.tads.pdm.myhelpenem.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {
    public static boolean CheckConnection(Context context) {
        ConnectivityManager ConnectionManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = ConnectionManager.getActiveNetworkInfo();
        if (info != null && info.isConnected() && info.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }
}

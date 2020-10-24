package com.example.childdraw;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternet {

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificom = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo phonecom = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wificom!=null && wificom.isConnected()) || (phonecom!=null && phonecom.isConnected())){
            return true;
        }else{
            return false;
        }
    }

}

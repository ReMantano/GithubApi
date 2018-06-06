package com.example.kotlingithubapi

import android.net.ConnectivityManager
import android.net.NetworkInfo

object CheckInternetConection{
    fun conect(cm: ConnectivityManager):Boolean{

        var status: NetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if(status != null && status.isConnected())  {

            return true
        }

        status = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if(status != null && status.isConnected())  {
            return true
        }



        return false
    }
}



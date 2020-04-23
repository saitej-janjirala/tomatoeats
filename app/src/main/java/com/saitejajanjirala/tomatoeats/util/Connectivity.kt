package com.saitejajanjirala.tomatoeats.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Connectivity {
    fun checkconnectivity(context: Context):Boolean{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activenetworkinfo:NetworkInfo ?= connectivityManager.activeNetworkInfo
        if(activenetworkinfo?.isConnected!=null){
            return activenetworkinfo.isConnected
        }
        else{
            return false
        }
    }
}
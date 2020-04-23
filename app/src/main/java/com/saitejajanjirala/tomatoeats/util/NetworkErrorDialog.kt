package com.saitejajanjirala.tomatoeats.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity

class NetworkErrorDialog(val context: Context) {
    fun createdialog(){
            val dialog= AlertDialog.Builder(context)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection")
            dialog.setPositiveButton("Open Settings"){
                    text,listener->
                val intent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                context.startActivity(intent)

            }
            dialog.setNegativeButton("exit"){
                    text,listener->
                ActivityCompat.finishAffinity(context as Activity)
            }
            dialog.create()
            dialog.show()


    }
}
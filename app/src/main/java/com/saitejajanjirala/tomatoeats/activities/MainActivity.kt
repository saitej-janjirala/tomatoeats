package com.saitejajanjirala.tomatoeats.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.saitejajanjirala.tomatoeats.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
            val sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE)
            if(sharedPreferences.contains("user_id")){
                val intent = Intent(this@MainActivity, ContentsActivity::class.java)
                intent.putExtra("user_id",sharedPreferences.getString("user_id",""))
                startActivity(intent)

            }else {
                val intent = Intent(this@MainActivity, Loginactivity::class.java)
                startActivity(intent)
            }
        },1500)

    }
}

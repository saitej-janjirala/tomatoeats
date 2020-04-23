package com.saitejajanjirala.tomatoeats.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.saitejajanjirala.tomatoeats.R

class Placeordersuccessful : AppCompatActivity() {

    lateinit var okbutton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placeordersuccessful)
        okbutton=findViewById(R.id.ok)
        okbutton.setOnClickListener {
            startActivity(Intent(this@Placeordersuccessful,ContentsActivity::class.java))
        }
    }

    override fun onBackPressed() {

    }
}

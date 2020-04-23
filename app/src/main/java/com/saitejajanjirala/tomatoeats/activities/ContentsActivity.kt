package com.saitejajanjirala.tomatoeats.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.fragments.*
import kotlinx.android.synthetic.main.activity_contents.*
import java.lang.Exception

class ContentsActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce=false
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents)
        drawerLayout=findViewById(R.id.drawerlayout)
        navigationView=findViewById(R.id.navigationview)
        var previousmenuitem: MenuItem?=null
        toolbar=findViewById(R.id.toolbar)
        settoolbar()
        openhome()
        try {
            val view2:View=navigationView.getHeaderView(0)
            val sharedpreferences=getSharedPreferences("user", Context.MODE_PRIVATE)
            val textnameheader:TextView=view2.findViewById(R.id.usernameinheader)
            textnameheader.text = sharedpreferences?.getString("name", "")
            val phonenumberinheader:TextView=view2.findViewById(R.id.phonenumberinheader)
            phonenumberinheader.text=sharedpreferences?.getString("mobile_number","")
        }
        catch (e:Exception){
            Log.d("usernamehader",e.message.toString())
        }
        navigationView.setNavigationItemSelectedListener {
            if(previousmenuitem!=null){
                previousmenuitem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousmenuitem=it
            when(it.itemId){
                R.id.Home->{
                    openhome()
                }
                R.id.Profile->{
                    navigationView.setCheckedItem(R.id.Profile)
                    supportFragmentManager.beginTransaction().replace(
                        R.id.container,
                        Profile()
                    ).commit()
                    supportActionBar?.title="Home"
                }
                R.id.Favourites->{
                    navigationView.setCheckedItem(R.id.Favourites)
                    supportFragmentManager.beginTransaction().replace(
                        R.id.container,
                        Favourites()
                    ).commit()
                    supportActionBar?.title="Favourites"
                }
                R.id.orderhistory->{
                    navigationView.setCheckedItem(R.id.orderhistory)
                    supportFragmentManager.beginTransaction().replace(
                        R.id.container,
                        OrderHistory()
                    ).commit()
                    supportActionBar?.title="History"
                }
                R.id.faqs->{
                    navigationView.setCheckedItem(R.id.faqs)
                    supportFragmentManager.beginTransaction().replace(
                        R.id.container,
                        faqs()
                    ).commit()
                    supportActionBar?.title="FAQS"
                }
                R.id.logout->{
                    val alertDialog=AlertDialog.Builder(this@ContentsActivity)
                    alertDialog.setTitle("Confirmation")
                    alertDialog.setMessage("Are you sure you want to Logout?")
                    alertDialog.setNegativeButton("no"){text,listener->
                    }
                    alertDialog.setPositiveButton("yes"){text,listener->
                        val sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE)
                        sharedPreferences.edit().clear().apply()
                        startActivity(Intent(this@ContentsActivity,Loginactivity::class.java))
                    }
                    alertDialog.create()
                    alertDialog.show()


                }
            }
            drawerLayout.closeDrawer(navigationview,true)
            return@setNavigationItemSelectedListener true


        }
    }
    fun settoolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title=""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val toggle= ActionBarDrawerToggle(this@ContentsActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }
    fun openhome(){
        navigationView.setCheckedItem(R.id.Home)
        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            Home()
        ).commit()
        supportActionBar?.title="All Restaurants"
    }

    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(navigationView,true)
        return true

    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.container)
        when(frag){
            !is Home -> openhome()
            else->ActivityCompat.finishAffinity(this@ContentsActivity)
        }
    }

}

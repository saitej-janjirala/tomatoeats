package com.saitejajanjirala.tomatoeats.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.adapters.CartAdapter
import com.saitejajanjirala.tomatoeats.adapters.RestaurantmenuAdapter
import com.saitejajanjirala.tomatoeats.model.Restaurantmenu
import com.saitejajanjirala.tomatoeats.model.Returnlmenulist
import java.lang.Exception
import kotlin.properties.Delegates

class Cart : AppCompatActivity() {
   lateinit var restaurantname:TextView
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var placeorder:Button
    lateinit var cartadapter:CartAdapter
    var totalprice=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        supportActionBar?.title="Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        restaurantname=findViewById(R.id.orderrestaurant)
        recyclerView=findViewById(R.id.cartrecyclerview)
        placeorder=findViewById(R.id.placeorder)
        val listobject = intent.getParcelableExtra<Returnlmenulist>(RestaurantmenuAdapter.MENU)
        val list=listobject?.mlist
        restaurantname.text=listobject?.resname
        Log.d("list","$list")
            cartadapter = CartAdapter(this@Cart, list, placeorder)
            cartadapter.setadapter(cartadapter)
            cartadapter.settext()
            layoutManager = LinearLayoutManager(this@Cart)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = cartadapter

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}

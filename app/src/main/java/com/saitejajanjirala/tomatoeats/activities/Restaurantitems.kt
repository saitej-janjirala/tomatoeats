package com.saitejajanjirala.tomatoeats.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.adapters.RestaurantmenuAdapter
import com.saitejajanjirala.tomatoeats.model.Restaurantmenu
import com.saitejajanjirala.tomatoeats.util.Connectivity
import com.saitejajanjirala.tomatoeats.util.NetworkErrorDialog
import org.json.JSONObject
import java.lang.Exception

class Restaurantitems : AppCompatActivity() {
    lateinit var progresslayout:RelativeLayout
    lateinit var mrecyclerView: RecyclerView
    lateinit var arraylist:ArrayList<Restaurantmenu>
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter:RestaurantmenuAdapter
    lateinit var proceeedtocart:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurantitems)
        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        supportActionBar?.title = name
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mrecyclerView = findViewById(R.id.menurecyclerview)
        progresslayout = findViewById(R.id.progresslayoutmenu)
        proceeedtocart=findViewById(R.id.proceedtocart)
        progresslayout.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this@Restaurantitems)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$id"
        if (Connectivity().checkconnectivity(this@Restaurantitems)){
            try {
                val jsonobjectrequest = object :
                    JsonObjectRequest(Request.Method.GET, url, null, Response.Listener<JSONObject> {
                        val successobject=it.getJSONObject("data")
                        Log.d("menuitems","$successobject")
                        val success=successobject.getBoolean("success")
                        try {
                            if (success) {
                                progresslayout.visibility = View.GONE
                                val jsonarray = successobject.getJSONArray("data")
                                arraylist = ArrayList()
                                for (i in 0..jsonarray.length() - 1) {
                                    val jsonobject = jsonarray.getJSONObject(i)
                                    val restaurantmenu = Restaurantmenu(
                                        jsonobject.getString("id"),
                                        jsonobject.getString("name"),
                                        jsonobject.getString("cost_for_one"),
                                        jsonobject.getString("restaurant_id"),
                                        false
                                    )
                                    arraylist.add(restaurantmenu)
                                }
                                adapter = RestaurantmenuAdapter(this@Restaurantitems, arraylist,proceeedtocart,name)
                                adapter.setadapter(adapter)
                                layoutManager = LinearLayoutManager(this@Restaurantitems)
                                mrecyclerView.layoutManager = layoutManager
                                mrecyclerView.adapter = adapter
                            } else {
                                progresslayout.visibility = View.GONE
                                Toast.makeText(
                                    this@Restaurantitems,
                                    successobject.getString("errorMessage"),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        catch (e:Exception){
                            progresslayout.visibility = View.GONE
                            Toast.makeText(this@Restaurantitems, e.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }, Response.ErrorListener {
                        progresslayout.visibility=View.GONE
                        Toast.makeText(this@Restaurantitems, it.message.toString(), Toast.LENGTH_LONG).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "0f768ec1585de2"
                        //de2
                        return headers
                    }
                }
                queue.add(jsonobjectrequest)
            } catch (e: Exception) {
                progresslayout.visibility=View.GONE
                Toast.makeText(this@Restaurantitems, e.message.toString(), Toast.LENGTH_LONG).show()
            }
    }
    else{
            progresslayout.visibility=View.GONE
            NetworkErrorDialog(this@Restaurantitems).createdialog()

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            try {
                if (adapter.checkifcangoback()) {
                    startActivity(Intent(this@Restaurantitems, ContentsActivity::class.java))
                } else {
                    val alert3 = AlertDialog.Builder(this@Restaurantitems)
                    alert3.setTitle("Confirmation")
                    alert3.setMessage("Going back will reset cart items. Do you still want to proceed?")
                    alert3.setNegativeButton("No") { text, listener -> }
                    alert3.setPositiveButton("Yes") { text, listener ->
                        startActivity(Intent(this@Restaurantitems, ContentsActivity::class.java))
                    }
                    alert3.create()
                    alert3.show()
                }
            }
            catch (e:Exception){
                Toast.makeText(this@Restaurantitems, e.message.toString(), Toast.LENGTH_LONG).show()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}

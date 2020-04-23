package com.saitejajanjirala.tomatoeats.fragments

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
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
import com.saitejajanjirala.tomatoeats.adapters.RestaurantsAdapter
import com.saitejajanjirala.tomatoeats.model.Restaurants
import com.saitejajanjirala.tomatoeats.util.Connectivity
import com.saitejajanjirala.tomatoeats.util.NetworkErrorDialog
import org.json.JSONObject
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Home : Fragment(){
    lateinit var mrecyclerview:RecyclerView
    lateinit var progresslayout:RelativeLayout
    lateinit var arraylistrestaurants:ArrayList<Restaurants>
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter:RestaurantsAdapter
    val array= arrayOf("Cost(Low to High)","Cost(High to low)","Rating")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_home, container, false)
        progresslayout=view.findViewById(R.id.progresslayout)
        mrecyclerview=view.findViewById(R.id.homerecyclerview)
        progresslayout.visibility=View.VISIBLE
        if(Connectivity().checkconnectivity(activity as Context)){
            val queue=Volley.newRequestQueue(activity as Context)
            val url="http://13.235.250.119/v2/restaurants/fetch_result/"
            try {
                val jsonrequest = object :
                    JsonObjectRequest(Request.Method.GET, url, null, Response.Listener<JSONObject> {
                        val successobject = it.getJSONObject("data")
                        val success = successobject.getBoolean("success")
                        arraylistrestaurants = ArrayList()
                        if (success) {
                            progresslayout.visibility = View.GONE
                            val jsonArray = successobject.getJSONArray("data")
                            for (i in 0..jsonArray.length() - 1) {
                                val ob = jsonArray.getJSONObject(i)
                                val restaurant = Restaurants(
                                    ob.getString("id"),
                                    ob.getString("name"),
                                    ob.getString("rating"),
                                    ob.getString("cost_for_one"),
                                    ob.getString("image_url"),
                                    favourite = false
                                )
                                arraylistrestaurants.add(restaurant)
                            }
                            adapter= RestaurantsAdapter(activity as Context,arraylistrestaurants)
                            adapter.setadapter(adapter)
                            layoutManager=LinearLayoutManager(activity as Context)
                            mrecyclerview.layoutManager=layoutManager
                            mrecyclerview.adapter=adapter

                        }
                        else{
                            progresslayout.visibility=View.GONE
                            Toast.makeText(activity as Context,successobject.getString("errorMessage"),Toast.LENGTH_LONG).show()
                        }

                    }, Response.ErrorListener {
                        progresslayout.visibility = View.GONE

                        Log.d("error", it.message.toString())
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "0f768ec1585de2"
                        //de2
                        return headers
                    }
                }
                queue.add(jsonrequest)
            }
            catch (e:Exception){
                Log.d("exception",e.message.toString())
            }
        }
        else{
            NetworkErrorDialog(activity as Context).createdialog()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==R.id.sortmenuitem){
            val alertdialog=AlertDialog.Builder(activity as Context)
            alertdialog.setTitle("Sort By?")
            alertdialog.setSingleChoiceItems(array,-1,DialogInterface.OnClickListener { dialogInterface, i ->
                when(i){
                    0->{
                        val lowtohigh=Comparator<Restaurants>{restaurant1,restaurant2->
                            restaurant1.cost_for_one.toInt().compareTo(restaurant2.cost_for_one.toInt())
                        }
                        Collections.sort(arraylistrestaurants,lowtohigh)
                    }
                    1->{
                        val hightolow=Comparator<Restaurants>{restaurant1,restaurant2->
                            restaurant1.cost_for_one.toInt().compareTo(restaurant2.cost_for_one.toInt())
                        }
                        Collections.sort(arraylistrestaurants,hightolow)
                        arraylistrestaurants.reverse()

                    }
                    2->{
                        try {
                            val rating = Comparator<Restaurants> { restaurants1, restaurants2 ->
                                if (restaurants1.rating.toFloat().compareTo(restaurants2.rating.toFloat()) == 0) {
                                    restaurants1.name.compareTo(restaurants2.name)
                                } else {
                                    restaurants1.rating.toFloat()
                                        .compareTo(restaurants2.rating.toFloat())
                                }
                            }
                            Collections.sort(arraylistrestaurants, rating)
                            arraylistrestaurants.reverse()
                        }
                        catch (e:Exception){
                            Toast.makeText(activity as Context,e.message.toString(),Toast.LENGTH_LONG).show()
                        }

                    }
                }
            })
            alertdialog.setCancelable(true)
            alertdialog.setPositiveButton("apply"){text,listener->
                adapter.notifyDataSetChanged()

            }
            alertdialog.create()
            alertdialog.show()
        }
        return super.onOptionsItemSelected(item)
    }



}

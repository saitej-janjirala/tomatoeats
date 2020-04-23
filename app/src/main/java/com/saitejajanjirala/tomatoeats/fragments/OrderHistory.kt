package com.saitejajanjirala.tomatoeats.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.adapters.OrderhistorymainAdapter
import com.saitejajanjirala.tomatoeats.model.Orderhistorymain
import com.saitejajanjirala.tomatoeats.model.Orderhistorysub
import com.saitejajanjirala.tomatoeats.util.Connectivity
import com.saitejajanjirala.tomatoeats.util.NetworkErrorDialog
import org.json.JSONObject
import java.lang.Exception


class OrderHistory : Fragment() {
    lateinit var shoppingcartrelative:RelativeLayout
    lateinit var mrecyclerview:RecyclerView
    lateinit var layoutmanager:LinearLayoutManager
    lateinit var arraylist:ArrayList<Orderhistorymain>
    lateinit var subarraylist:ArrayList<Orderhistorysub>
    lateinit var madapter:OrderhistorymainAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_order_history, container, false)
        mrecyclerview=view.findViewById(R.id.previousordersrecyclerview)
        shoppingcartrelative=view.findViewById(R.id.shoppingcartrelative)
        if(Connectivity().checkconnectivity(activity as Context)){
            val queue=Volley.newRequestQueue(activity as Context)
            val sharedPreferences= context?.getSharedPreferences("user",Context.MODE_PRIVATE)
            val userd= sharedPreferences?.getString("user_id","")
            val url="http://13.235.250.119/v2/orders/fetch_result/$userd"
            try {
                val jsonrequest = object :
                    JsonObjectRequest(Request.Method.GET, url, null, Response.Listener<JSONObject> {
                            val successobject=it.getJSONObject("data")
                            Log.d("successobjectlist","$successobject")
                            val success=successobject.getBoolean("success")
                            arraylist= ArrayList()
                            if(success){
                                try {
                                    val jsonarray = successobject.getJSONArray("data")
                                    for (i in 0..jsonarray.length() - 1) {
                                        val jsonObj = jsonarray.getJSONObject(i)
                                        subarraylist = ArrayList()
                                        val jsonsubarray = jsonObj.getJSONArray("food_items")
                                        for (j in 0..jsonsubarray.length() - 1) {
                                            val jsonsubObj = jsonsubarray.getJSONObject(j)
                                            val orderhistorysub = Orderhistorysub(
                                                jsonsubObj.getString("food_item_id"),
                                                jsonsubObj.getString("name"),
                                                jsonsubObj.getString("cost")
                                            )
                                            subarraylist.add(orderhistorysub)
                                        }

                                        val obj = Orderhistorymain(
                                            jsonObj.getString("order_id"),
                                            jsonObj.getString("restaurant_name"),
                                            jsonObj.getString("total_cost"),
                                            jsonObj.getString("order_placed_at"),
                                            subarraylist
                                        )
                                        arraylist.add(obj)
                                    }
                                }
                                catch (e:Exception){
                                    Toast.makeText(activity as Context,e.message.toString(),Toast.LENGTH_LONG).show()
                                }
                                if (arraylist.size == 0) {
                                    shoppingcartrelative.visibility = View.VISIBLE
                                } else {
                                    Log.d("arraylistofmain", "$arraylist")

                                    layoutmanager = LinearLayoutManager(activity as Context)
                                    madapter = OrderhistorymainAdapter(
                                        activity as Context,
                                        arraylist
                                    )
                                    mrecyclerview.layoutManager = layoutmanager
                                    mrecyclerview.adapter = madapter
                                    mrecyclerview.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

                                }
                            }
                            else{
                                Toast.makeText(activity as Context,successobject.getString("errorMessage"),Toast.LENGTH_LONG).show()
                            }
                    }, Response.ErrorListener {
                        Toast.makeText(activity as Context,it.message.toString(),Toast.LENGTH_LONG).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "0f768ec1585de2"
                        return headers
                    }
                }
                queue.add(jsonrequest)
            }
            catch (e:Exception){
                Toast.makeText(activity as Context,e.message.toString(),Toast.LENGTH_LONG).show()
            }
        }
        else{
            NetworkErrorDialog(activity as Context).createdialog()
        }

        return view
    }





}

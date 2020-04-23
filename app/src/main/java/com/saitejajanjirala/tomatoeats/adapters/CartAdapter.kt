package com.saitejajanjirala.tomatoeats.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.activities.Placeordersuccessful
import com.saitejajanjirala.tomatoeats.model.Restaurantmenu
import com.saitejajanjirala.tomatoeats.util.Connectivity
import com.saitejajanjirala.tomatoeats.util.NetworkErrorDialog
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class CartAdapter(val context:Context,val arraylist:ArrayList<Restaurantmenu>?,val placeorder:Button):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    lateinit var madapter:CartAdapter
    var totalsum:Int=0
    class CartViewHolder(view: View):RecyclerView.ViewHolder(view){
        val xremove:ImageView=view.findViewById(R.id.remove)
        val xitemname:TextView=view.findViewById(R.id.itemname)
        val xitemcost:TextView=view.findViewById(R.id.itemcost)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
            val view=LayoutInflater.from(context).inflate(R.layout.exampleitemforcart,parent,false)
            return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
            return arraylist!!.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.xitemname.text = arraylist!!.get(position).name

        val a = arraylist.get(position).cost_for_one
        holder.xitemcost.text = "Rs. $a"
        holder.xremove.setOnClickListener {
            totalsum -= arraylist.get(position).cost_for_one!!.toInt()
            if (totalsum == 0) {
                placeorder.visibility = View.GONE
            }
            arraylist.remove(arraylist.get(position))
            madapter.notifyDataSetChanged()
            settext()
        }
        placeorder.setOnClickListener {
            if (Connectivity().checkconnectivity(context)) {
                try {
                    val queue = Volley.newRequestQueue(context)
                    val url = "http://13.235.250.119/v2/place_order/fetch_result/"
                    val sharedPreferences =
                        context.getSharedPreferences("user", Context.MODE_PRIVATE)
                    val user_id = sharedPreferences.getString("user_id", "")
                    val jsonpararry = JSONArray()
                    for (i in 0..arraylist.size - 1) {
                        val json1 = JSONObject()
                        json1.put("food_item_id", arraylist.get(i).id)
                        jsonpararry.put(json1)
                    }
                    val jsonparams = JSONObject()
                    jsonparams.put("user_id", user_id)
                    jsonparams.put("restaurant_id", arraylist.get(0).restaurant_id)
                    jsonparams.put("total_cost", "$totalsum")
                    jsonparams.put("food", jsonpararry)
                    try {
                        val jsonrequest = object : JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonparams,
                            Response.Listener<JSONObject> {
                                val jsondataObject = it.getJSONObject("data")
                                val success = jsondataObject.getBoolean("success")
                                if (success) {
                                    val intent = Intent(context, Placeordersuccessful::class.java)
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        context,
                                        jsondataObject.getString("errorMessage"),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            },
                            Response.ErrorListener {
                                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG)
                                    .show()
                            }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "0f768ec1585de2"
                                return headers
                            }

                        }
                        queue.add(jsonrequest)

                    } catch (e: Exception) {
                        Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
                }

            } else {
                NetworkErrorDialog(context).createdialog()
            }
        }
    }
        fun setadapter(adapter: CartAdapter) {
            madapter = adapter
            for (i in 0..arraylist!!.size - 1) {
                totalsum += arraylist.get(i).cost_for_one!!.toInt()
            }
        }

        @SuppressLint("SetTextI18n")
        fun settext() {
            placeorder.text = "Place Order(Total:Rs.$totalsum)"
        }

}
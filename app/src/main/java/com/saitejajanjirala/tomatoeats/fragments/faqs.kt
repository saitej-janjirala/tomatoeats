package com.saitejajanjirala.tomatoeats.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.adapters.FaqAdapter
import com.saitejajanjirala.tomatoeats.model.FaqsEntities
import java.lang.Exception


class faqs : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutmanager:LinearLayoutManager
    lateinit var madapter:FaqAdapter
    lateinit var arrayList: ArrayList<FaqsEntities>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

            val view = inflater.inflate(R.layout.fragment_faqs, container, false)
        try{
            recyclerView = view.findViewById(R.id.faqrecyclerview)
            arrayList = ArrayList()
            arrayList.add(
                FaqsEntities(
                    "How does this app work?",
                    "You just have to login to our app if not register with valid credntials,then the home screen will display a list of restaurants select one if you want to mark its as favourite you can then select the food items that you want to deliver and then place your order and also you can see the preious orders to know about your orders. "
                )
            )
            arrayList.add(
                FaqsEntities(
                    "Do you have a free delivery?",
                    "Most of the restaurants wont charge fee for delivery but some retsaurants charges Rs.30 in case they are far away from your location."
                )
            )
            arrayList.add(
                FaqsEntities(
                    "Are the prices different than at the restaurant?",
                    "We do have a serice fee of 10% for our non-patnered restaurants."
                )
            )
            arrayList.add(
                FaqsEntities(
                    "How long does it take for a deliery?",
                    "Our normal Delivery time is between 45 minutes and 1 hour,however certain situations such as traffic,weather and restaurant preparation time requires extra time.Please know that we are working very hard to get your food as quick as possible.We appreciate your patience.Placing orders in advance is appreciated."
                )
            )
            arrayList.add(
                FaqsEntities(
                    "what is your Refund Policy?",
                    "The customer is financially responsible for payment once an order is submitted. If you want to change your order, we will attempt to accommodate such wishes within the time constraints and the good will of the participating restaurants. However, if a change is too late to process, you are responsible for payment of the original order. Changes to orders must be phoned to the staff. Our e-mail is not checked often enough to ensure that we get your changes."
                )
            )
            Log.d("dbarraylist","$arrayList")
            try{
                madapter = FaqAdapter(activity as Context, arrayList)
            layoutmanager = LinearLayoutManager(activity as Context)
            recyclerView.layoutManager = layoutmanager
                recyclerView.adapter = madapter
                recyclerView.addItemDecoration(DividerItemDecoration(activity as Context,LinearLayoutManager.VERTICAL))
                }
            catch (e:Exception){
                Toast.makeText(activity as Context,e.message.toString(),Toast.LENGTH_LONG).show()
            }

        }
        catch (e:Exception){
            Toast.makeText(activity as Context,e.message.toString(),Toast.LENGTH_LONG).show()
        }

        return view
    }


}

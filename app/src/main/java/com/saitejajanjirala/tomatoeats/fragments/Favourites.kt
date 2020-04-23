package com.saitejajanjirala.tomatoeats.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.adapters.RestaurantsAdapter
import com.saitejajanjirala.tomatoeats.database.RestaurantDatabase
import com.saitejajanjirala.tomatoeats.database.Restaurantentities
import com.saitejajanjirala.tomatoeats.model.Restaurants


class Favourites : Fragment() {

    lateinit var favouriterecyclerview:RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter:RestaurantsAdapter
    lateinit var arrayList: ArrayList<Restaurants>
    lateinit var shownothin:RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favourites, container, false)
        favouriterecyclerview=view.findViewById(R.id.favouritesrecyclerview)
        shownothin=view.findViewById(R.id.shownothing)
        try {
            layoutManager = LinearLayoutManager(activity as Context)
            arrayList = ArrayList()

            val list: List<Restaurantentities>? = Db(activity as Context).execute().get()
            for (i in 0..list!!.size - 1) {
                val x: Restaurantentities = list.get(i)
                val obj: Restaurants =
                    Restaurants(x.id, x.name, x.rating, x.cost_for_one, x.image_url, false)
                arrayList.add(obj)
            }
            if(arrayList.size==0){
                shownothin.visibility=View.VISIBLE
            }
            else{
                    shownothin.visibility=View.GONE
                    adapter = RestaurantsAdapter(activity as Context, arrayList)
                    adapter.setadapter(adapter)
                    adapter.setforfavourites(shownothin)
                    favouriterecyclerview.layoutManager = layoutManager
                    favouriterecyclerview.adapter = adapter
            }
        }
        catch (e:Exception){
            Toast.makeText(activity as Context,e.message.toString(),Toast.LENGTH_LONG).show()
        }
        return view
    }
    class Db(context: Context):AsyncTask<Void,Void,List<Restaurantentities>>(){
        val sharedPreferences=context.getSharedPreferences("user",Context.MODE_PRIVATE)
        val db=Room.databaseBuilder(context,RestaurantDatabase::class.java,sharedPreferences.getString("user_id","")!!).build()
        override fun doInBackground(vararg p0: Void?):List<Restaurantentities> {
            return db.restaurantDao().getallrestaurants()
        }
    }
}

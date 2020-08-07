package com.saitejajanjirala.tomatoeats.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.activities.Restaurantitems
import com.saitejajanjirala.tomatoeats.database.RestaurantDatabase
import com.saitejajanjirala.tomatoeats.database.Restaurantentities
import com.saitejajanjirala.tomatoeats.model.Restaurants
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

class RestaurantsAdapter(val context:Context, val arrayList: ArrayList<Restaurants>) :RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>(){
    lateinit var madapter:RestaurantsAdapter
    lateinit var mview:RelativeLayout
    var valforfavourites=false
    interface onitemclicklistener{
        fun onfavouriteclicked()
    }
    class RestaurantsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rimageview:ImageView=itemView.findViewById(R.id.restaurantimage)
        val rname:TextView=itemView.findViewById(R.id.restaurantname)
        val rrating:TextView=itemView.findViewById(R.id.restaurantrating)
        val rfavourite:ImageView=itemView.findViewById(R.id.favouriteimage)
        val rprice:TextView=itemView.findViewById(R.id.restaurantprice)
        val cardView:CardView=itemView.findViewById(R.id.restaurantcard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        val view:View=LayoutInflater.from(context).inflate(R.layout.restaurantexampleitem,parent,false)
        return RestaurantsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
        var obj=arrayList.get(position)
        Picasso.get().load(obj.image_url).into(holder.rimageview)
        holder.rname.text=obj.name
        val price=obj.cost_for_one
        holder.rprice.text="₹$price/person"
        holder.rrating.text=obj.rating
        val entities1=Restaurantentities(obj.id,obj.name,obj.rating,obj.cost_for_one,obj.image_url)
        if(Dbtask(context,entities1,3).execute().get()){
            holder.rfavourite.setImageResource(R.drawable.ic_favorite_black_24dp)
        }
        else{
            holder.rfavourite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }
        holder.rfavourite.setOnClickListener {
            val entities=Restaurantentities(obj.id,obj.name,obj.rating,obj.cost_for_one,obj.image_url)
            if(position!=RecyclerView.NO_POSITION) {
                if(Dbtask(context,entities,3).execute().get()){
                    Dbtask(context,entities,2).execute()
                    if(valforfavourites){
                        arrayList.remove(obj)
                        try {
                            if (arrayList.size == 0) {
                                mview.visibility = View.VISIBLE
                            }
                        }
                        catch (e:Exception){
                            Log.d("exception",e.message.toString())
                        }
                    }
                    madapter.notifyDataSetChanged()
                }
                else{
                    Dbtask(context,entities,1).execute()
                    madapter.notifyDataSetChanged()
                }
            }

        }
        holder.cardView.setOnClickListener{
            val  intent=Intent(context,Restaurantitems::class.java)
            intent.putExtra("id",obj.id)
            intent.putExtra("name",obj.name)
            context.startActivity(intent)
        }
    }
    fun setadapter(adapter:RestaurantsAdapter){
        madapter=adapter

    }
    fun setforfavourites(layout: RelativeLayout){
        mview=layout
        valforfavourites=true
    }
    class Dbtask(val context: Context,val restaurantentities: Restaurantentities,val mode:Int): AsyncTask<Void, Void, Boolean>() {
        /*
        for mode
        create the database name with user_id.db
        1->insert in the data base
        2->delete from the database
        3->check if exists
        4->getall
         */
        val sharedPreferences=context.getSharedPreferences("user",Context.MODE_PRIVATE)
        val dbname=sharedPreferences.getString("user_id","")
        val db=Room.databaseBuilder(context,RestaurantDatabase::class.java,dbname!!).build()
        var boolean by Delegates.notNull<Boolean>()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1->{
                    db.restaurantDao().insertrestaurant(restaurantentities)
                    db.close()
                    boolean= true
                }
                2->{
                    db.restaurantDao().deleterestaurant(restaurantentities)
                    db.close()
                    boolean=true
                }
                3->{
                    val res:Restaurantentities?=db.restaurantDao().getrestaurantbyid(restaurantentities.id)
                    db.close()
                    boolean = res!=null
                }
                4->{
                    val list=db.restaurantDao().getallrestaurants()
                    Log.d("database list","$list")
                    boolean=true
                }
            }
            return boolean

        }
    }


}
package com.saitejajanjirala.tomatoeats.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.activities.Cart
import com.saitejajanjirala.tomatoeats.model.Restaurantmenu
import com.saitejajanjirala.tomatoeats.model.Returnlmenulist
import kotlin.properties.Delegates

class RestaurantmenuAdapter(val context: Context, val arraylist:ArrayList<Restaurantmenu>,val proceedtocart:Button,val resname:String):RecyclerView.Adapter<RestaurantmenuAdapter.RestaurantmenuViewHolder>() {
    lateinit var madapter:RestaurantmenuAdapter
     val marraylist:ArrayList<Restaurantmenu> = ArrayList()
    var total by Delegates.notNull<Int>()
    var selected=0
    companion object{
        const val MENU="menulist"
    }
    class RestaurantmenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val zsno: TextView = view.findViewById(R.id.sno)
        val zname: TextView = view.findViewById(R.id.name)
        val zprice: TextView = view.findViewById(R.id.price)
        val zaddbutton: Button = view.findViewById(R.id.addbutton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantmenuViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.menuitems,parent,false)
        return RestaurantmenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arraylist.size
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onBindViewHolder(holder: RestaurantmenuViewHolder, position: Int) {
        arraylist.get(position)
        val pos=position+1
        holder.zsno.text="$pos"
        holder.zname.text=arraylist.get(position).name
        val rs=arraylist.get(position).cost_for_one
        holder.zprice.text="Rs.$rs"
        if(arraylist.get(position).added!!){
            holder.zaddbutton.setBackgroundColor(context.getColor(R.color.gold))
            holder.zaddbutton.text="Remove"
        }
        else{
            holder.zaddbutton.setBackgroundColor(context.getColor(R.color.colorPrimary))
            holder.zaddbutton.text="Add"
        }
        holder.zaddbutton.setOnClickListener {
            if(arraylist.get(position).added!!){
                arraylist.get(position).added=false
                madapter.notifyDataSetChanged()
                selected-=1
                marraylist.remove(arraylist.get(position))
                if(selected>0){
                    proceedtocart.visibility=View.VISIBLE
                }
                else{
                    proceedtocart.visibility=View.GONE
                }
            }
            else{
                arraylist.get(position).added=true
                madapter.notifyDataSetChanged()
                selected+=1
                marraylist.add(arraylist.get(position))
                proceedtocart.visibility=View.VISIBLE
            }
        }
        proceedtocart.setOnClickListener {
            try {
                Log.d("my arraylist","$marraylist")
                val intent = Intent(context.applicationContext, Cart::class.java)
                val obj=Returnlmenulist(resname,marraylist)
                intent.putExtra(MENU, obj)
                context.startActivity(intent)
            }
            catch (e:Exception){

                Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
            }

        }
    }
    fun setadapter(adapter: RestaurantmenuAdapter){
        madapter=adapter
    }
    fun checkifcangoback():Boolean{
        return selected <= 0

    }
}


package com.saitejajanjirala.tomatoeats.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.model.Orderhistorymain

class OrderhistorymainAdapter(val context: Context,val arrayList: ArrayList<Orderhistorymain>) :RecyclerView.Adapter<OrderhistorymainAdapter.OrderhistorymainViewHolder>(){
    class OrderhistorymainViewHolder(view: View):RecyclerView.ViewHolder(view){
        val resname:TextView=view.findViewById(R.id.resname)
        val date:TextView=view.findViewById(R.id.date)
        val totalcost:TextView=view.findViewById(R.id.totalcost)
        val recyclerView:RecyclerView=view.findViewById(R.id.subrecyclerview)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderhistorymainViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.exampleitemfororderhistorymain,parent,false)
        return OrderhistorymainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderhistorymainViewHolder, position: Int) {
        val name=arrayList.get(position).restaurant_name
        val price=arrayList.get(position).total_cost
        holder.resname.text=name
        holder.totalcost.text="Total cost: Rs.$price"
        val x= arrayList.get(position).order_placed_at.split(" ")
        holder.date.text=x[0].replace("-","/")
        val xadapter=OrderhistorysubAdapter(context,arrayList.get(position).itemslist)
        holder.recyclerView.layoutManager=LinearLayoutManager(context)
        holder.recyclerView.adapter=xadapter



    }
}
package com.saitejajanjirala.tomatoeats.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.model.Orderhistorysub

class OrderhistorysubAdapter (val context: Context,val arrayList: ArrayList<Orderhistorysub>):RecyclerView.Adapter<OrderhistorysubAdapter.OrderhistorysubViewHolder>(){
    class OrderhistorysubViewHolder(view: View):RecyclerView.ViewHolder(view){
        val itemname:TextView=view.findViewById(R.id.itemname)
        val itemprice:TextView=view.findViewById(R.id.itemprice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderhistorysubViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.exampleitemfororderhistorysub,parent,false)
        return OrderhistorysubViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderhistorysubViewHolder, position: Int) {
        holder.itemname.text=arrayList.get(position).name
        val xprice=arrayList.get(position).cost
        holder.itemprice.text="Rs.$xprice"
    }
}
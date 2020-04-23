package com.saitejajanjirala.tomatoeats.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.model.FaqsEntities

class FaqAdapter(val context: Context,val arrayList: ArrayList<FaqsEntities>):RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {
    class FaqViewHolder(view:View):RecyclerView.ViewHolder(view){
        val question:TextView=view.findViewById(R.id.question)
        val answer:TextView=view.findViewById(R.id.answer)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.exampleitemforfaq,parent,false)
        return FaqViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        Log.d("inside adapter","$arrayList")
        val quest=arrayList.get(position).question
        val pos=position+1
        holder.question.text="Q.$pos  $quest"
        val ans=arrayList.get(position).answer
        holder.answer.text="A.$pos  $ans"

    }
}
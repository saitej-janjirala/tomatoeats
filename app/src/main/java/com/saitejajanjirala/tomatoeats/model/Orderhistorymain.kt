package com.saitejajanjirala.tomatoeats.model

data class Orderhistorymain(val order_id:String,val restaurant_name:String,val total_cost:String,val order_placed_at:String,val itemslist:ArrayList<Orderhistorysub>)
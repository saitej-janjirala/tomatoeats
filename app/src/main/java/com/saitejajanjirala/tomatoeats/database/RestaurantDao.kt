package com.saitejajanjirala.tomatoeats.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {
    @Insert
    fun insertrestaurant(restaurantentities: Restaurantentities)
    @Delete
    fun deleterestaurant(restaurantentities: Restaurantentities)
    @Query("select * from restaurants")
    fun getallrestaurants():List<Restaurantentities>

    @Query("select * from restaurants where id=:restaurant_id")
    fun getrestaurantbyid(restaurant_id:String):Restaurantentities

}
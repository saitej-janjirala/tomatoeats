package com.saitejajanjirala.tomatoeats.model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Restaurantmenu(
    val id: String?, val name: String?, val cost_for_one: String?, val restaurant_id: String?, var added:Boolean?):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(cost_for_one)
        parcel.writeString(restaurant_id)
        parcel.writeValue(added)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Restaurantmenu> {
        override fun createFromParcel(parcel: Parcel): Restaurantmenu {
            return Restaurantmenu(parcel)
        }

        override fun newArray(size: Int): Array<Restaurantmenu?> {
            return arrayOfNulls(size)
        }
    }

}
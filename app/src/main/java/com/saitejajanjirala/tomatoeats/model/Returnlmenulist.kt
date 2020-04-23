package com.saitejajanjirala.tomatoeats.model

import android.os.Parcel
import android.os.Parcelable

data class Returnlmenulist(val resname:String?,val mlist:ArrayList<Restaurantmenu>?):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(Restaurantmenu.CREATOR)

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(resname)
        parcel.writeTypedList(mlist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Returnlmenulist> {
        override fun createFromParcel(parcel: Parcel): Returnlmenulist {
            return Returnlmenulist(parcel)
        }

        override fun newArray(size: Int): Array<Returnlmenulist?> {
            return arrayOfNulls(size)
        }
    }

}
package com.saitejajanjirala.tomatoeats.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class FaqsEntities (
     val question:String,
    val answer:String)
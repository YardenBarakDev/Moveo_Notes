package com.ybdev.moveonotes.mvvm.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey
    @ColumnInfo(name = "key")val key: String,
    @ColumnInfo(name = "userID") val userID : String,
    @ColumnInfo(name = "timeStamp")val timeStamp : Long,
    @ColumnInfo(name = "title")var title : String,
    @ColumnInfo(name = "text")var text : String,
    @ColumnInfo(name = "locationLon")val locationLon : Double,
    @ColumnInfo(name = "locationLat")val locationLat : Double,
    var image: Bitmap?
) : Parcelable

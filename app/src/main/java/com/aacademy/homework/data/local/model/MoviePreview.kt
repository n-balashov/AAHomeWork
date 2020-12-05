package com.aacademy.homework.data.local.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoviePreview(
    @PrimaryKey val id: Int,
    val title: String,
    val coverPath: String,
    val ageLimit: Int,
    val rating: Int,
    val reviews: Int,
    val min: Int,
    var isLiked: Boolean,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(coverPath)
        parcel.writeInt(ageLimit)
        parcel.writeInt(rating)
        parcel.writeInt(reviews)
        parcel.writeInt(min)
        parcel.writeByte(if (isLiked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<MoviePreview> {

        override fun createFromParcel(parcel: Parcel): MoviePreview {
            return MoviePreview(parcel)
        }

        override fun newArray(size: Int): Array<MoviePreview?> {
            return arrayOfNulls(size)
        }
    }
}
package com.example.projectmanagement.model

import android.os.Parcel
import android.os.Parcelable

data class Card(
    val name: String = "",
    val createdBy: String = "",
    var labelColor:String ="",
    val assignedTo: ArrayList<String> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: ArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(createdBy)
        parcel.writeString(labelColor)
        parcel.writeStringList(assignedTo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }
}

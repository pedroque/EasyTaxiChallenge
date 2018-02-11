package com.pedroabinajm.easytaxichallenge.data.model

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class EasyPlace : RealmObject, Parcelable {
    @PrimaryKey
    lateinit var id: String

    lateinit var name: String

    var latitude = 0.0

    var longitude = 0.0

    var description: String? = null

    var bookmark: Boolean = false

    constructor()

    constructor(id: String, name: String, latitude: Double, longitude: Double, description: String?,
                bookmark: Boolean) {
        this.id = id
        this.name = name
        this.latitude = latitude
        this.longitude = longitude
        this.description = description
        this.bookmark = bookmark
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readString(),
            source.readByte() == 1.toByte()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeDouble(latitude)
        writeDouble(longitude)
        writeString(description)
        writeByte(if (bookmark) 1 else 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<EasyPlace> = object : Parcelable.Creator<EasyPlace> {
            override fun createFromParcel(source: Parcel): EasyPlace = EasyPlace(source)
            override fun newArray(size: Int): Array<EasyPlace?> = arrayOfNulls(size)
        }
    }
}
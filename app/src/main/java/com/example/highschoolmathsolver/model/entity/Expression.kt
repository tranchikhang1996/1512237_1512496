package com.example.highschoolmathsolver.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Expression")
data class Expression (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long = 0,
    @ColumnInfo(name = "expression") val expression: String? = null,
    @ColumnInfo(name = "date") val date: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(expression)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Expression> {
        override fun createFromParcel(parcel: Parcel): Expression {
            return Expression(parcel)
        }

        override fun newArray(size: Int): Array<Expression?> {
            return arrayOfNulls(size)
        }
    }
}
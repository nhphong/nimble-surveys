package com.nhphong.nimblesurveys.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Survey(
  @PrimaryKey
  val id: String = "",

  @SerializedName("title")
  @ColumnInfo(name = "title")
  val name: String = "",

  @SerializedName("description")
  @ColumnInfo(name = "description")
  val description: String = "",

  @SerializedName("cover_image_url")
  @ColumnInfo(name = "cover_image_url")
  val coverImageUrl: String = ""
) : Parcelable {

  constructor(src: Parcel) : this(
    src.readString() ?: "",
    src.readString() ?: "",
    src.readString() ?: "",
    src.readString() ?: ""
  )

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel?, flags: Int) {
    dest?.writeString(id)
    dest?.writeString(name)
    dest?.writeString(description)
    dest?.writeString(coverImageUrl)
  }

  companion object CREATOR : Parcelable.Creator<Survey> {
    override fun createFromParcel(parcel: Parcel): Survey {
      return Survey(parcel)
    }

    override fun newArray(size: Int): Array<Survey?> {
      return arrayOfNulls(size)
    }
  }
}

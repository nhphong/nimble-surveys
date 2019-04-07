package com.nhphong.nimblesurveys.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Survey(
  @SerializedName("id")
  val id: String = "",

  @SerializedName("title")
  val name: String = "",

  @SerializedName("description")
  val description: String = "",

  @SerializedName("cover_image_url")
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

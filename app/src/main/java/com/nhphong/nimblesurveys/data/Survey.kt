package com.nhphong.nimblesurveys.data

import com.google.gson.annotations.SerializedName

data class Survey(
  @SerializedName("title")
  val name: String = "",

  @SerializedName("description")
  val description: String = "",

  @SerializedName("cover_image_url")
  val coverImageUrl: String = ""
)

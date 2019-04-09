package com.nhphong.nimblesurveys.data

import com.google.gson.annotations.SerializedName

data class Credentials(
  @SerializedName("grant_type")
  val grantType: String = "",

  @SerializedName("username")
  val userName: String = "",

  @SerializedName("password")
  val password: String = ""
)

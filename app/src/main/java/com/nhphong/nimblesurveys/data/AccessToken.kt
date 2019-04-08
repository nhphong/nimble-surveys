package com.nhphong.nimblesurveys.data

import com.google.gson.annotations.SerializedName

data class AccessToken(
  @SerializedName("access_token")
  val data: String = "",

  @SerializedName("token_type")
  val type: String = "",

  @SerializedName("expires_in")
  val expiresIn: Long = 0L,

  @SerializedName("created_at")
  val createdAt: Long = 0L
)

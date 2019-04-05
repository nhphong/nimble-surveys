package com.nhphong.nimblesurveys.data

data class AccessToken(
  val data: String = "",
  val type: String = "",
  val expiresIn: Long = 0L,
  val createdAt: Long = 0L
)

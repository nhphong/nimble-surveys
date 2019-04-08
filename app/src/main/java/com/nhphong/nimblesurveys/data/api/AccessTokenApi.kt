package com.nhphong.nimblesurveys.data.api

import com.nhphong.nimblesurveys.data.AccessToken
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

interface AccessTokenApi {

  @POST("oauth/token")
  fun renewAccessToken(
    @Query("grant_type")
    grantType: String = DEFAULT_GRANT_TYPE,

    @Query("username")
    userName: String = DEFAULT_USERNAME,

    @Query("password")
    password: String = DEFAULT_PASSWORD
  ): Observable<AccessToken>

  companion object {
    const val DEFAULT_GRANT_TYPE = "password"
    const val DEFAULT_USERNAME = "carlos@nimbl3.com"
    //TODO encrypt the following sensitive data
    const val DEFAULT_PASSWORD = "antikera"
  }
}

package com.nhphong.nimblesurveys.data.api

import com.nhphong.nimblesurveys.data.AccessToken
import com.nhphong.nimblesurveys.data.Credentials
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

private const val DEFAULT_GRANT_TYPE = "password"
private const val DEFAULT_USERNAME = "carlos@nimbl3.com"
private const val DEFAULT_PASSWORD = "antikera"

interface AccessTokenApi {

  @POST("oauth/token")
  fun renewAccessToken(
    @Body credentials: Credentials = Credentials(
      grantType = DEFAULT_GRANT_TYPE,
      userName = DEFAULT_USERNAME,
      password = DEFAULT_PASSWORD
    )
  ): Observable<AccessToken>
}

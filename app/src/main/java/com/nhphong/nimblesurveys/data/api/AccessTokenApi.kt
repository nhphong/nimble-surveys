package com.nhphong.nimblesurveys.data.api

import com.nhphong.nimblesurveys.data.AccessToken
import com.nhphong.nimblesurveys.data.Credentials
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface AccessTokenApi {

  @POST("oauth/token")
  fun renewAccessToken(
    @Body credentials: Credentials = Credentials(
      grantType = DEFAULT_GRANT_TYPE,
      userName = DEFAULT_USERNAME,
      password = DEFAULT_PASSWORD
    )
  ): Observable<AccessToken>

  companion object {
    const val DEFAULT_GRANT_TYPE = "password"
    const val DEFAULT_USERNAME = "carlos@nimbl3.com"
    //TODO encrypt the following sensitive data, or save it to a private local storage (SharedPreferences)
    const val DEFAULT_PASSWORD = "antikera"
  }
}

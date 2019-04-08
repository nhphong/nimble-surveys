package com.nhphong.nimblesurveys.data.gateways.external

import com.nhphong.nimblesurveys.data.AccessToken
import com.nhphong.nimblesurveys.data.api.AccessTokenApi
import io.reactivex.Single
import javax.inject.Inject

// APIs that don't require access token
interface FreeApiGateway {
  fun renewAccessToken(): Single<AccessToken>
}

class FreeApiGatewayImpl @Inject constructor(
  private val accessTokenApi: AccessTokenApi
) : FreeApiGateway {
  override fun renewAccessToken(): Single<AccessToken> {
    return accessTokenApi.renewAccessToken().firstOrError()
  }
}

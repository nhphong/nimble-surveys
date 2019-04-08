package com.nhphong.nimblesurveys.data

import com.nhphong.nimblesurveys.data.gateways.external.FreeApiGateway
import com.nhphong.nimblesurveys.data.gateways.local.SharedPreferencesGateway
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

interface UserRepository {
  fun saveAccessToken(accessToken: AccessToken): Completable
  fun loadAccessToken(): Maybe<AccessToken>
  fun invalidateAccessToken(): Completable
  fun renewAccessToken(): Single<AccessToken>
}

class UserRepositoryImpl @Inject constructor(
  private val sharedPreferencesGateway: SharedPreferencesGateway,
  private val apiGateway: FreeApiGateway
) : UserRepository {
  override fun saveAccessToken(accessToken: AccessToken): Completable {
    return sharedPreferencesGateway.saveAccessToken(accessToken)
  }

  override fun loadAccessToken(): Maybe<AccessToken> {
    return sharedPreferencesGateway.loadAccessToken()
  }

  override fun invalidateAccessToken(): Completable {
    return sharedPreferencesGateway.invalidateAccessToken()
  }

  override fun renewAccessToken(): Single<AccessToken> {
    return apiGateway.renewAccessToken()
      .doOnSuccess {
        // Update Access Token in the local storage
        sharedPreferencesGateway.saveAccessToken(it)
      }
  }
}

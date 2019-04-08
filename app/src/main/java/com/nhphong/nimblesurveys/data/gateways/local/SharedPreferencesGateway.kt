package com.nhphong.nimblesurveys.data.gateways.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.nhphong.nimblesurveys.data.AccessToken
import com.nhphong.nimblesurveys.data.gateways.local.SharedPreferencesGateway.Companion.ACCESS_TOKEN
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

interface SharedPreferencesGateway {
  fun saveAccessToken(accessToken: AccessToken): Completable
  fun loadAccessToken(): Maybe<AccessToken>
  fun invalidateAccessToken(): Completable

  companion object {
    internal const val ACCESS_TOKEN = "access_token"
  }
}

class SharedPreferencesGatewayImpl @Inject constructor(
  private val gson: Gson,
  private val preferences: SharedPreferences
) : SharedPreferencesGateway {

  override fun saveAccessToken(accessToken: AccessToken): Completable {
    return Completable.fromCallable {
      preferences.edit().putString(
        ACCESS_TOKEN, gson.toJson(accessToken)
      ).apply()
    }
  }

  override fun loadAccessToken(): Maybe<AccessToken> {
    return Maybe.just(Unit)
      .flatMap {
        val jsonContent = preferences.getString(ACCESS_TOKEN, "")
        val accessToken: AccessToken? = gson.fromJson(jsonContent, AccessToken::class.java)
        if (accessToken != null) {
          Maybe.just(accessToken)
        } else {
          Maybe.empty()
        }
      }
  }

  override fun invalidateAccessToken(): Completable {
    return Completable.fromCallable {
      preferences.edit().putString(
        ACCESS_TOKEN, ""
      ).apply()
    }
  }
}

package com.nhphong.nimblesurveys.data

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.nhphong.nimblesurveys.data.gateways.external.FreeApiGateway
import com.nhphong.nimblesurveys.data.gateways.local.SharedPreferencesGateway
import io.reactivex.Single
import org.junit.Test

class UserRepositoryImplTest {

  private val token = AccessToken()
  private val sharedPreferencesGateway: SharedPreferencesGateway = mock()
  private val apiGateway: FreeApiGateway = mock()
  private val repository = UserRepositoryImpl(sharedPreferencesGateway, apiGateway)

  @Test
  fun saveAccessToken() {
    // see the test in SharedPreferencesGatewayImplTest#saveAccessToken_happyCase()
    repository.saveAccessToken(token)
    verify(sharedPreferencesGateway).saveAccessToken(token)
  }

  @Test
  fun loadAccessToken() {
    // see the test in SharedPreferencesGatewayImplTest#loadAccessToken_happyCase()
    repository.loadAccessToken()
    verify(sharedPreferencesGateway).loadAccessToken()
  }

  @Test
  fun invalidateAccessToken() {
    // see the test in SharedPreferencesGatewayImplTest#invalidateAccessToken_happyCase()
    repository.invalidateAccessToken()
    verify(sharedPreferencesGateway).invalidateAccessToken()
  }

  @Test
  fun renewAccessToken() {
    val newToken = AccessToken()
    doReturn(Single.just(newToken)).whenever(apiGateway).renewAccessToken()

    repository.renewAccessToken()
      .test()
      .awaitCount(1)
      .assertValue(newToken)

    verify(apiGateway).renewAccessToken()
    verify(sharedPreferencesGateway).saveAccessToken(newToken)
  }
}

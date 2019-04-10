package com.nhphong.nimblesurveys.data.gateways.local

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.nhphong.nimblesurveys.data.AccessToken
import com.nhphong.nimblesurveys.data.gateways.local.SharedPreferencesGateway.Companion.ACCESS_TOKEN
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.application
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23], manifest = Config.NONE)
class SharedPreferencesGatewayImplTest {

  private lateinit var preferences: SharedPreferences
  private lateinit var gateway: SharedPreferencesGateway

  @Before
  fun setup() {
    preferences = application.getSharedPreferences("test", MODE_PRIVATE).apply {
      edit().clear().commit()
    }
    gateway = SharedPreferencesGatewayImpl(Gson(), preferences)
  }

  @Test
  fun saveAccessToken_happyCase() {
    gateway.saveAccessToken(accessToken)
      .test()
      .assertComplete()

    val expectedJson = """
      {
          "access_token": "token-data",
          "token_type": "bearer",
          "expires_in": 7200,
          "created_at": 1485174186
      }
      """
    val actualJson = preferences.getString(ACCESS_TOKEN, "")
    assertEquals(expectedJson.noWhiteSpaces(), actualJson)
  }

  @Test
  fun saveAccessToken_unexpectedErrorOccurs() {
    preferences = mock()
    gateway = SharedPreferencesGatewayImpl(Gson(), preferences)

    val error = NullPointerException("Something went wrong")
    doThrow(error).whenever(preferences).edit()

    gateway.saveAccessToken(accessToken)
      .test()
      .assertError(error)
  }

  @Test
  fun loadAccessToken_happyCase() {
    preferences.edit()
      .putString(ACCESS_TOKEN, Gson().toJson(accessToken))
      .apply()

    gateway.loadAccessToken()
      .test()
      .awaitCount(1)
      .assertValue(accessToken)
  }

  @Test
  fun loadAccessToken_thereIsNothingInSharedPreferences() {
    gateway.loadAccessToken()
      .test()
      .assertComplete()
      .assertNoValues()
  }

  @Test
  fun loadAccessToken_unexpectedErrorOccurs() {
    // Access token is malformed
    preferences.edit()
      .putString(ACCESS_TOKEN, "access token")
      .apply()

    gateway.loadAccessToken()
      .test()
      .assertError {
        it is JsonSyntaxException
      }
  }

  @Test
  fun invalidateAccessToken_happyCase() {
    gateway.saveAccessToken(accessToken)
      .test()
      .assertComplete()

    gateway.loadAccessToken()
      .test()
      .awaitCount(1)
      .assertValue(accessToken)

    gateway.invalidateAccessToken()
      .test()
      .assertComplete()

    gateway.loadAccessToken()
      .test()
      .assertComplete()
      .assertNoValues()
  }

  @Test
  fun invalidateAccessToken_unexpectedErrorOccurs() {
    preferences = mock()
    gateway = SharedPreferencesGatewayImpl(Gson(), preferences)

    val error = NullPointerException("Something went wrong")
    doThrow(error).whenever(preferences).edit()

    gateway.invalidateAccessToken()
      .test()
      .assertError(error)
  }

  private fun String.noWhiteSpaces(): String {
    return this.replace(Regex("\\s+"), "")
  }

  private companion object TestData {
    val accessToken = AccessToken("token-data", "bearer", 7200L, 1485174186L)
  }
}

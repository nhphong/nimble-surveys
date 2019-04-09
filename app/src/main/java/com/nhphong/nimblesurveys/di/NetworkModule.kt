package com.nhphong.nimblesurveys.di

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nhphong.nimblesurveys.BuildConfig
import com.nhphong.nimblesurveys.data.AccessToken
import com.nhphong.nimblesurveys.data.UserRepository
import com.nhphong.nimblesurveys.utils.fullMessage
import dagger.Module
import dagger.Provides
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
  @Provides
  fun gson() = GsonBuilder()
    .setLenient()
    .create()

  @Provides
  fun okHttpClient(userRepository: UserRepository): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor {
        val accessToken = getAccessToken(userRepository)
        it.proceed(addQueryParam(it.request(), accessToken))
      }
      .authenticator(authenticator(userRepository))
      .build()
  }

  @Provides
  @Singleton
  @WithInterceptor
  fun retrofitWithInterceptor(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .baseUrl(BuildConfig.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(okHttpClient)
      .build()
  }

  @Provides
  @Singleton
  @WithoutInterceptor
  fun retrofit(gson: Gson): Retrofit {
    return Retrofit.Builder()
      .baseUrl(BuildConfig.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
  }

  private fun addQueryParam(request: Request, accessToken: AccessToken): Request {
    return request.newBuilder()
      .url(
        request.url()
          .newBuilder()
          .addQueryParameter("access_token", accessToken.data)
          .build()
      )
      .build()
  }

  private fun authenticator(userRepository: UserRepository): Authenticator {
    return Authenticator { _, response ->
      val failedToken = response.request().url().queryParameter("access_token")
      var newToken: AccessToken? = null

      try {
        newToken = userRepository.renewAccessToken().blockingGet()
      } catch (e: Exception) {
        Log.e("NetworkModule", "onResponse: ${e.fullMessage()}")
      }

      if (newToken == null || newToken.data.isEmpty() || newToken.data == failedToken) {
        null
      } else {
        // retry the failed 401 request with new access token
        addQueryParam(response.request(), newToken)
      }
    }
  }

  private fun getAccessToken(userRepository: UserRepository): AccessToken {
    return try {
      userRepository.loadAccessToken().blockingGet(AccessToken())
    } catch (e: Exception) {
      Log.e("GlobalModule", e.fullMessage())
      AccessToken()
    }
  }
}

package com.nhphong.nimblesurveys.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nhphong.nimblesurveys.BuildConfig
import com.nhphong.nimblesurveys.data.AccessToken
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
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
  fun okHttpClient(accessToken: AccessToken): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor { chain ->
        with(chain.request()) {
          this.newBuilder()
            .url(
              this.url()
                .newBuilder()
                .addQueryParameter("access_token", accessToken.data)
                .build()
            )
            .build()
        }.let {
          chain.proceed(it)
        }
      }
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
}

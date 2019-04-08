package com.nhphong.nimblesurveys.di

import com.nhphong.nimblesurveys.data.api.AccessTokenApi
import com.nhphong.nimblesurveys.data.api.SurveysApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ApiModule {
  @Provides
  fun surveysApi(@WithInterceptor retrofit: Retrofit) = retrofit.create(SurveysApi::class.java)

  @Provides
  fun accessTokenApi(@WithoutInterceptor retrofit: Retrofit) = retrofit.create(AccessTokenApi::class.java)
}

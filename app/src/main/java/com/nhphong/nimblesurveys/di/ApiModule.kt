package com.nhphong.nimblesurveys.di

import com.nhphong.nimblesurveys.data.api.SurveysApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ApiModule {
  @Provides
  fun surveysApi(retrofit: Retrofit) = retrofit.create(SurveysApi::class.java)
}

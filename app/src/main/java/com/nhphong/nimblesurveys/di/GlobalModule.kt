package com.nhphong.nimblesurveys.di

import com.nhphong.nimblesurveys.data.SurveysRepository
import com.nhphong.nimblesurveys.data.SurveysRepositoryImpl
import com.nhphong.nimblesurveys.data.gateways.local.DatabaseGateway
import com.nhphong.nimblesurveys.data.gateways.local.DatabaseGatewayImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [UserModule::class])
interface GlobalModule {
  @Binds
  fun databaseGateway(databaseGateway: DatabaseGatewayImpl): DatabaseGateway

  @Binds
  @Singleton
  fun surveysRepository(surveysRepository: SurveysRepositoryImpl): SurveysRepository
}

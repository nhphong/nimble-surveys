package com.nhphong.nimblesurveys.di

import com.nhphong.nimblesurveys.data.gateways.external.ApiGateway
import com.nhphong.nimblesurveys.data.gateways.external.ApiGatewayImpl
import com.nhphong.nimblesurveys.data.gateways.external.FreeApiGateway
import com.nhphong.nimblesurveys.data.gateways.external.FreeApiGatewayImpl
import com.nhphong.nimblesurveys.data.gateways.local.DatabaseGateway
import com.nhphong.nimblesurveys.data.gateways.local.DatabaseGatewayImpl
import com.nhphong.nimblesurveys.data.gateways.local.SharedPreferencesGateway
import com.nhphong.nimblesurveys.data.gateways.local.SharedPreferencesGatewayImpl
import dagger.Binds
import dagger.Module

@Module(includes = [StorageModule::class, ApiModule::class])
interface GatewayModule {
  @Binds
  fun sharedPreferencesGateway(sharedPreferencesGateway: SharedPreferencesGatewayImpl): SharedPreferencesGateway

  @Binds
  fun databaseGateway(databaseGateway: DatabaseGatewayImpl): DatabaseGateway

  @Binds
  fun apiGateway(apiGateway: ApiGatewayImpl): ApiGateway

  @Binds
  fun freeApiGateway(freeApiGateway: FreeApiGatewayImpl): FreeApiGateway
}

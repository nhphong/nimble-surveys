package com.nhphong.nimblesurveys.di

import com.nhphong.nimblesurveys.data.gateways.external.ApiGateway
import com.nhphong.nimblesurveys.data.gateways.external.ApiGatewayImpl
import com.nhphong.nimblesurveys.data.gateways.local.DatabaseGateway
import com.nhphong.nimblesurveys.data.gateways.local.DatabaseGatewayImpl
import dagger.Binds
import dagger.Module

@Module(includes = [ApiModule::class])
interface GatewayModule {
  @Binds
  fun databaseGateway(databaseGateway: DatabaseGatewayImpl): DatabaseGateway

  @Binds
  fun apiGateway(apiGateway: ApiGatewayImpl): ApiGateway
}

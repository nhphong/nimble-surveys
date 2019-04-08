package com.nhphong.nimblesurveys.di

import com.nhphong.nimblesurveys.data.SurveysRepository
import com.nhphong.nimblesurveys.data.SurveysRepositoryImpl
import com.nhphong.nimblesurveys.data.UserRepository
import com.nhphong.nimblesurveys.data.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [GatewayModule::class])
interface RepositoryModule {
  @Binds
  @Singleton
  fun surveysRepository(surveysRepository: SurveysRepositoryImpl): SurveysRepository

  @Binds
  @Singleton
  fun userRepository(userRepository: UserRepositoryImpl): UserRepository
}

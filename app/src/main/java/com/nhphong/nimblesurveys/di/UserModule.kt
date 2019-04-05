package com.nhphong.nimblesurveys.di

import com.nhphong.nimblesurveys.data.AccessToken
import com.nhphong.nimblesurveys.data.UserRepository
import com.nhphong.nimblesurveys.data.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserModule {
  @Provides
  @Singleton
  fun userRepository(): UserRepository {
    return UserRepositoryImpl()
  }

  @Provides
  fun accessToken(userRepository: UserRepository): AccessToken {
    // The access token is stored in SharedPreferences, so it is quick enough to retrieve from the main thread
    // We can use blockingGet() here without having to worry about its performance
    // TODO find a better way!
    return userRepository.getAccessToken().blockingGet()
  }
}

package com.nhphong.nimblesurveys.di

import com.nhphong.nimblesurveys.data.AccessToken
import com.nhphong.nimblesurveys.data.UserRepository
import dagger.Module
import dagger.Provides

@Module
class GlobalModule {
  @Provides
  fun accessToken(userRepository: UserRepository): AccessToken {
    // The access token is stored in SharedPreferences, so it is quick enough to retrieve from the main thread
    // We can use blockingGet() here without having to worry about the ANR issue (Application Not Responding)
    // TODO find a better way!
    return userRepository.getAccessToken().blockingGet()
  }
}

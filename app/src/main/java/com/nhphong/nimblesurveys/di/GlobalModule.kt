package com.nhphong.nimblesurveys.di

import android.content.Context
import com.nhphong.nimblesurveys.App
import com.nhphong.nimblesurveys.data.AccessToken
import com.nhphong.nimblesurveys.data.UserRepository
import com.nhphong.nimblesurveys.utils.StringResProvider
import com.nhphong.nimblesurveys.utils.StringResProviderImpl
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class GlobalModule {
  @Provides
  fun accessToken(userRepository: UserRepository): AccessToken {
    // The access token is stored in SharedPreferences, so it is quick enough to retrieve from the main thread
    // We can use blockingGet() here without having to worry about the ANR issue (Application Not Responding)
    // TODO find a better way!
    return userRepository.loadAccessToken().blockingGet(AccessToken())
  }

  @Provides
  @MainScheduler
  fun mainScheduler(): Scheduler {
    return AndroidSchedulers.mainThread()
  }

  @Provides
  @IOScheduler
  fun ioScheduler(): Scheduler {
    return Schedulers.io()
  }

  @Provides
  @Singleton
  @ApplicationContext
  fun applicationContext(app: App): Context {
    return app
  }

  @Provides
  fun stringResProvider(@ApplicationContext context: Context): StringResProvider {
    return StringResProviderImpl(context.resources)
  }
}

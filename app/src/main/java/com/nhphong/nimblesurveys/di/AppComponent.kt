package com.nhphong.nimblesurveys.di.scope

import com.nhphong.nimblesurveys.App
import com.nhphong.nimblesurveys.di.ActivityBindingModule
import com.nhphong.nimblesurveys.di.GlobalModule
import com.nhphong.nimblesurveys.di.NetworkModule
import com.nhphong.nimblesurveys.di.RepositoryModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AndroidInjectionModule::class,
    ActivityBindingModule::class,
    RepositoryModule::class,
    NetworkModule::class,
    GlobalModule::class
  ]
)
interface AppComponent : AndroidInjector<App> {
  @Component.Builder
  abstract class Builder : AndroidInjector.Builder<App>()
}

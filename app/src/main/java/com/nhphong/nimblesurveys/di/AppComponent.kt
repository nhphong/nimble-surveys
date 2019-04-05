package com.nhphong.nimblesurveys.di.scope

import com.nhphong.nimblesurveys.App
import com.nhphong.nimblesurveys.di.ActivityBindingModule
import com.nhphong.nimblesurveys.di.GlobalModule
import com.nhphong.nimblesurveys.di.NetworkModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
  AndroidInjectionModule::class,
  ActivityBindingModule::class,
  GlobalModule::class,
  NetworkModule::class
])
interface AppComponent {
  fun inject(app: App)
}

package com.nhphong.nimblesurveys.di

import com.nhphong.nimblesurveys.di.scope.ActivityScope
import com.nhphong.nimblesurveys.views.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBindingModule {
  @ActivityScope
  @ContributesAndroidInjector(modules = [MainActivityModule::class])
  fun mainActivity(): MainActivity
}

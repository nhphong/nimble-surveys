package com.nhphong.nimblesurveys.di

import com.nhphong.nimblesurveys.di.scope.ActivityScope
import com.nhphong.nimblesurveys.views.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBindingModule {
  @ActivityScope
  @ContributesAndroidInjector(modules = [MainActivityModule::class, MainActivityFragmentBindingModule::class])
  fun mainActivity(): MainActivity
}

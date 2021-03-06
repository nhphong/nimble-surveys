package com.nhphong.nimblesurveys.di

import android.app.Activity
import com.nhphong.nimblesurveys.di.scope.ActivityScope
import com.nhphong.nimblesurveys.viewmodels.SurveysViewModel
import com.nhphong.nimblesurveys.viewmodels.SurveysViewModelImpl
import com.nhphong.nimblesurveys.views.activity.MainActivity
import dagger.Binds
import dagger.Module

@Module
interface MainActivityModule {
  @Binds
  @ActivityScope
  fun surveysViewModel(surveysViewModel: SurveysViewModelImpl): SurveysViewModel

  @Binds
  @ActivityScope
  fun activity(activity: MainActivity): Activity
}

package com.nhphong.nimblesurveys.di

import android.app.Activity
import com.nhphong.nimblesurveys.data.SurveysRepository
import com.nhphong.nimblesurveys.di.scope.ActivityScope
import com.nhphong.nimblesurveys.viewmodels.SurveysViewModel
import com.nhphong.nimblesurveys.viewmodels.SurveysViewModelImpl
import com.nhphong.nimblesurveys.views.activity.MainActivity
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class MainActivityModule {
  @Provides
  @ActivityScope
  fun surveysViewModel(surveysRepository: SurveysRepository): SurveysViewModel {
    return SurveysViewModelImpl(surveysRepository, AndroidSchedulers.mainThread(), Schedulers.io())
  }

  @Provides
  @ActivityScope
  fun activity(activity: MainActivity): Activity {
    return activity
  }
}

package com.nhphong.nimblesurveys.di

import com.nhphong.nimblesurveys.views.fragment.SurveyFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityFragmentBindingModule {

  @ContributesAndroidInjector
  fun surveyFragment(): SurveyFragment
}

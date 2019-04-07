package com.nhphong.nimblesurveys.views.activity

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector

class TestApp : Application(), HasActivityInjector {
  var activityInjector = AndroidInjector<Activity> {
    // do nothing
  }

  override fun activityInjector() = activityInjector
}

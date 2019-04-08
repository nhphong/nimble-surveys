package com.nhphong.nimblesurveys.rules

import android.app.Activity
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.nhphong.nimblesurveys.views.activity.TestApp
import dagger.android.AndroidInjector

class InjectedActivityTestRule<T : Activity>(
  activityClass: Class<T>,
  private val activityInjector: (T) -> Unit
) : IntentsTestRule<T>(activityClass, false, true) {

  override fun beforeActivityLaunched() {
    super.beforeActivityLaunched()
    getApplicationContext<TestApp>().apply {
      activityInjector = AndroidInjector { instance ->
        @Suppress("unchecked_cast")
        activityInjector(instance as T)
      }
    }
  }
}

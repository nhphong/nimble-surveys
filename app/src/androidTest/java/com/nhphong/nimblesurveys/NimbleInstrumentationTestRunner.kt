package com.nhphong.nimblesurveys

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.nhphong.nimblesurveys.views.activity.TestApp

class NimbleInstrumentationTestRunner: AndroidJUnitRunner() {
  override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
    return super.newApplication(cl, TestApp::class.java.name, context)
  }
}

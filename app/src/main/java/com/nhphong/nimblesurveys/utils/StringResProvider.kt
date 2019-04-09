package com.nhphong.nimblesurveys.utils

import android.content.res.Resources
import com.nhphong.nimblesurveys.R
import javax.inject.Inject

interface StringResProvider {
  val fetchingSurveys: String
  val noSurveysMessage: String
}

class StringResProviderImpl @Inject constructor(
  private val res: Resources
) : StringResProvider {

  override val fetchingSurveys: String
    get() = res.getString(R.string.fetching_surveys)

  override val noSurveysMessage: String
    get() = res.getString(R.string.no_surveys)
}

package com.nhphong.nimblesurveys.views.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nhphong.nimblesurveys.R
import kotlinx.android.synthetic.main.activity_survey_detail.*

class SurveyDetailActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_survey_detail)
    val surveyId = intent?.extras?.getString(EXTRA_SURVEY_ID) ?: getString(R.string.question_marks)
    name.text = getString(R.string.details_for_survey, surveyId)
  }

  companion object {
    const val EXTRA_SURVEY_ID = "extra-survey-id"
  }
}

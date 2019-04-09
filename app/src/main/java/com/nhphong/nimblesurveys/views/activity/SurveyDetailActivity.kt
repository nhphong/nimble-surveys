package com.nhphong.nimblesurveys.views.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.nhphong.nimblesurveys.R
import kotlinx.android.synthetic.main.activity_survey_detail.*

class SurveyDetailActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_survey_detail)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    val surveyId = intent?.extras?.getString(EXTRA_SURVEY_ID) ?: getString(R.string.question_marks)
    name.text = getString(R.string.details_for_survey, surveyId)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      android.R.id.home -> { finish() }
    }
    return super.onOptionsItemSelected(item)
  }

  companion object {
    const val EXTRA_SURVEY_ID = "extra-survey-id"
  }
}

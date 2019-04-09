package com.nhphong.nimblesurveys.views.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.nhphong.nimblesurveys.R
import kotlinx.android.synthetic.main.activity_survey_detail.*
import kotlinx.android.synthetic.main.toolbar.menu_button as menuButton
import kotlinx.android.synthetic.main.toolbar.reload_button as reloadButton
import kotlinx.android.synthetic.main.toolbar.toolbar_title as toolbarTitle

class SurveyDetailActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_survey_detail)
    setupActionBar()

    val surveyId = intent?.extras?.getString(EXTRA_SURVEY_ID) ?: getString(R.string.question_marks)
    name.text = getString(R.string.details_for_survey, surveyId)
  }

  private fun setupActionBar() {
    setSupportActionBar(toolbar as Toolbar)
    supportActionBar?.let {
      it.setDisplayHomeAsUpEnabled(true)
      it.setDisplayShowTitleEnabled(false)
    }
    toolbarTitle.text = getString(R.string.survey_details_title)
    reloadButton.visibility = GONE
    menuButton.visibility = GONE
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      android.R.id.home -> {
        finish()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  companion object {
    const val EXTRA_SURVEY_ID = "extra-survey-id"
  }
}

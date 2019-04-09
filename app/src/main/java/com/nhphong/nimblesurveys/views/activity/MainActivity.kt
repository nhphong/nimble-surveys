package com.nhphong.nimblesurveys.views.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.nhphong.nimblesurveys.R
import com.nhphong.nimblesurveys.navigators.SurveyItemNavigator
import com.nhphong.nimblesurveys.utils.EventObserver
import com.nhphong.nimblesurveys.utils.showSnackbar
import com.nhphong.nimblesurveys.viewmodels.SurveysViewModel
import com.nhphong.nimblesurveys.views.activity.SurveyDetailActivity.Companion.EXTRA_SURVEY_ID
import com.nhphong.nimblesurveys.views.adapter.MainPagerAdapter
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.message_text_view as messageTextView
import kotlinx.android.synthetic.main.activity_main.view_pager as viewPager
import kotlinx.android.synthetic.main.toolbar.reload_button as reloadButton
import kotlinx.android.synthetic.main.toolbar.toolbar_title as toolbarTitle

class MainActivity : AppCompatActivity(), SurveyItemNavigator, HasSupportFragmentInjector {

  @Inject
  lateinit var surveysViewModel: SurveysViewModel
  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AndroidInjection.inject(this)
    setContentView(R.layout.activity_main)
    setupActionBar()

    val adapter = MainPagerAdapter(supportFragmentManager).also {
      viewPager.adapter = it
    }

    with(surveysViewModel) {
      surveys.observe(this@MainActivity, Observer {
        adapter.surveys = it
      })

      message.observe(this@MainActivity, Observer {
        messageTextView.text = it
      })

      snackBarMessage.observe(this@MainActivity, EventObserver {
        viewPager.showSnackbar(it)
      })

      internalErrorMessage.observe(this@MainActivity, EventObserver {
        Log.e("MainActivity", it)
      })

      openSurveyEvent.observe(this@MainActivity, EventObserver {
        openSurveyDetails(it)
      })

      loadSurveys()
    }
  }

  private fun setupActionBar() {
    setSupportActionBar(toolbar as Toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)
    toolbarTitle.text = getString(R.string.surveys_title)
    reloadButton.setOnClickListener { surveysViewModel.reloadSurveys() }
  }

  override fun openSurveyDetails(surveyId: String) {
    startActivity(Intent(this, SurveyDetailActivity::class.java).apply {
      putExtra(EXTRA_SURVEY_ID, surveyId)
    })
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment> {
    if (::dispatchingAndroidInjector.isInitialized) {
      return dispatchingAndroidInjector
    }
    return AndroidInjector { }
  }
}

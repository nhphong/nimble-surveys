package com.nhphong.nimblesurveys.views.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.activity_main.view_pager as viewPager

class MainActivity : AppCompatActivity(), SurveyItemNavigator, HasSupportFragmentInjector {

  @Inject
  lateinit var surveysViewModel: SurveysViewModel
  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  private lateinit var adapter: MainPagerAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AndroidInjection.inject(this)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    adapter = MainPagerAdapter(supportFragmentManager).also {
      viewPager.adapter = it
    }

    with(surveysViewModel) {
      surveys.observe(this@MainActivity, Observer {
        adapter.surveys = it
      })

      errorMessages.observe(this@MainActivity, EventObserver {
        viewPager.showSnackbar(it)
      })

      openSurveyEvent.observe(this@MainActivity, EventObserver {
        openSurveyDetails(it)
      })
    }
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

package com.nhphong.nimblesurveys.views.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.nhphong.nimblesurveys.R
import com.nhphong.nimblesurveys.navigators.SurveyItemNavigator
import com.nhphong.nimblesurveys.utils.EventObserver
import com.nhphong.nimblesurveys.utils.showSnackbar
import com.nhphong.nimblesurveys.viewmodels.SurveysViewModel
import com.nhphong.nimblesurveys.views.activity.SurveyDetailActivity.Companion.EXTRA_SURVEY_ID
import com.nhphong.nimblesurveys.views.adapter.BulletsAdapter
import com.nhphong.nimblesurveys.views.adapter.MainPagerAdapter
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

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

    val pagerAdapter = MainPagerAdapter(supportFragmentManager)
    viewPager.adapter = pagerAdapter

    with(bullets) {
      layoutManager = LinearLayoutManager(this@MainActivity, VERTICAL, false)
      adapter = BulletsAdapter().apply {
        syncWithViewPager(viewPager, this@with)
      }
    }

    with(surveysViewModel) {
      surveys.observe(this@MainActivity, Observer {
        pagerAdapter.surveys = it
        val previousPage = savedInstanceState?.getInt(EXTRA_CURRENT_PAGE, -1) ?: -1
        if (previousPage != -1) {
          viewPager.currentItem = previousPage
          savedInstanceState?.clear()
        }
      })

      message.observe(this@MainActivity, Observer {
        this@MainActivity.message.text = it
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

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(EXTRA_CURRENT_PAGE, viewPager.currentItem)
  }

  private fun setupActionBar() {
    setSupportActionBar(toolbar as Toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)
    toolbarTitle.text = getString(R.string.surveys_title)
    reloadBtn.setOnClickListener { surveysViewModel.reloadSurveys() }
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

  private companion object {
    const val EXTRA_CURRENT_PAGE = "current page"
  }
}

package com.nhphong.nimblesurveys.views.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.nhphong.nimblesurveys.R
import com.nhphong.nimblesurveys.utils.showSnackbar
import com.nhphong.nimblesurveys.viewmodels.SurveysViewModel
import com.nhphong.nimblesurveys.views.adapter.MainPagerAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.view_pager as viewPager

class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var surveysViewModel: SurveysViewModel

  private lateinit var adapter: MainPagerAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    adapter = MainPagerAdapter(supportFragmentManager).also {
      viewPager.adapter = it
    }

    with(surveysViewModel) {
      surveys().observe(this@MainActivity, Observer {
        adapter.refreshData(it)
      })

      errorMessages().observe(this@MainActivity, Observer {
        viewPager.showSnackbar(it)
      })
    }
  }
}

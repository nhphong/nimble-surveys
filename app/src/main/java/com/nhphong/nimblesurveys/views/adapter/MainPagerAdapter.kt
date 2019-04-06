package com.nhphong.nimblesurveys.views.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.views.fragment.SurveyFragment

class MainPagerAdapter(
  fragmentManager: FragmentManager
): FragmentStatePagerAdapter(fragmentManager) {

  private val pages = List(3) { SurveyFragment() }
  private val surveys = arrayListOf<Survey>()

  fun refreshData(surveys: List<Survey>) {
    with(this.surveys) {
      clear()
      addAll(surveys)
    }
    pages.forEachIndexed { i, p ->
      p.updateSurvey(surveys[i])
    }
  }

  override fun getItem(position: Int): Fragment {
    return pages[position]
  }

  override fun getCount(): Int {
    return pages.size
  }
}

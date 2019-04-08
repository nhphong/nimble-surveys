package com.nhphong.nimblesurveys.views.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.views.fragment.SurveyFragment

class MainPagerAdapter(
  fragmentManager: FragmentManager
): FragmentStatePagerAdapter(fragmentManager) {

  var surveys: List<Survey> = emptyList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  private val availablePages by lazy {
    List(NUM_AVAILABLE_PAGES) {
      SurveyFragment()
    }
  }

  override fun getItem(position: Int): Fragment {
    return availablePages[position % NUM_AVAILABLE_PAGES].apply {
      setSurvey(surveys[position])
    }
  }

  override fun getCount(): Int {
    return surveys.size
  }

  companion object {
    const val NUM_AVAILABLE_PAGES = 4
  }
}

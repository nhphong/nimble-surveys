package com.nhphong.nimblesurveys.views.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.views.fragment.SurveyFragment

class MainPagerAdapter(
  fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

  var surveys: List<Survey> = emptyList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun getItem(position: Int): Fragment {
    return SurveyFragment.newInstance(surveys[position])
  }

  override fun getCount(): Int {
    return surveys.size
  }
}

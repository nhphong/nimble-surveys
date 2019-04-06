package com.nhphong.nimblesurveys.views.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.nhphong.nimblesurveys.R
import com.nhphong.nimblesurveys.data.Survey
import kotlinx.android.synthetic.main.fragment_survey.view.*
import kotlinx.android.synthetic.main.fragment_survey.view.take_survey_btn as takeSurveyBtn
import kotlinx.android.synthetic.main.fragment_survey.view.cover_image as coverImage

class SurveyFragment: Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_survey, container, false).also {
      it.description.movementMethod = ScrollingMovementMethod()
      it.takeSurveyBtn.setOnClickListener {  }
    }
  }

  fun updateSurvey(survey: Survey) {
    if (activity?.isFinishing == false && isAdded) {
      view?.apply {
        name.text = survey.name
        description.text = survey.description

        Glide.with(this@SurveyFragment)
          .load(survey.coverImageUrl)
          .centerCrop()
          .into(coverImage)
      }
    }
  }
}

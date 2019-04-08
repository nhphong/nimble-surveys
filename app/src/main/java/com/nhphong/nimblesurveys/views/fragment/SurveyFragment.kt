package com.nhphong.nimblesurveys.views.fragment

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nhphong.nimblesurveys.R
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.di.GlideApp
import com.nhphong.nimblesurveys.viewmodels.SurveysViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_survey.view.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_survey.view.cover_image as coverImage
import kotlinx.android.synthetic.main.fragment_survey.view.take_survey_btn as takeSurveyBtn

class SurveyFragment : Fragment(), View.OnClickListener {

  @Inject
  lateinit var surveysViewModel: SurveysViewModel

  private var _view: View? = null
  private val _survey: Survey?
    get() = arguments?.getParcelable(EXTRA_ARG_SURVEY)

  override fun onAttach(context: Context) {
    super.onAttach(context)
    AndroidSupportInjection.inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    if (_view == null) {
      _view = inflater.inflate(R.layout.fragment_survey, container, false).apply {
        description.movementMethod = ScrollingMovementMethod()
        takeSurveyBtn.setOnClickListener(this@SurveyFragment)
      }
    }

    return _view.also {
      renderUI(it, _survey)
    }
  }

  override fun onClick(v: View?) {
    _survey?.let {
      surveysViewModel.openSurvey(it.id)
    }
  }

  fun setSurvey(survey: Survey) {
    arguments = Bundle().apply {
      putParcelable(EXTRA_ARG_SURVEY, survey)
    }
  }

  private fun renderUI(view: View?, survey: Survey?) {
    if (view != null && activity?.isFinishing == false && isAdded) {
      view.name.text = survey?.name ?: ""
      view.description.text = survey?.description ?: ""

      GlideApp.with(this)
        .load(survey?.coverImageUrl ?: "")
        .placeholder(R.color.white)
        .centerCrop()
        .into(view.coverImage)
    }
  }

  companion object {
    const val EXTRA_ARG_SURVEY = "survey"
  }
}
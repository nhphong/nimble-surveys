package com.nhphong.nimblesurveys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.data.SurveysRepository
import javax.inject.Inject

abstract class SurveysViewModel : ViewModel() {
  abstract fun getSurveys(): LiveData<List<Survey>>
}

class SurveysViewModelImpl @Inject constructor(
  private val surveysRepository: SurveysRepository
) : SurveysViewModel() {

  private val _surveys = MutableLiveData<List<Survey>>()

  override fun getSurveys(): LiveData<List<Survey>> {
    return _surveys
  }
}

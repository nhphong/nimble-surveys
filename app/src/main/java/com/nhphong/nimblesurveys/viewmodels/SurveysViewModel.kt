package com.nhphong.nimblesurveys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.data.SurveysRepository
import io.reactivex.Scheduler
import javax.inject.Inject

abstract class SurveysViewModel : ViewModel() {
  abstract fun surveys(): LiveData<List<Survey>>
  abstract fun errorMessages(): LiveData<String>
}

class SurveysViewModelImpl @Inject constructor(
  private val surveysRepository: SurveysRepository,
  private val mainScheduler: Scheduler,
  private val ioScheduler: Scheduler
) : SurveysViewModel() {

  private val _surveys: MutableLiveData<List<Survey>> by lazy {
    MutableLiveData<List<Survey>>().also {
      loadSurveys()
    }
  }
  private val _errorMessages = MutableLiveData<String>()

  override fun surveys(): LiveData<List<Survey>> {
    return _surveys
  }

  override fun errorMessages(): LiveData<String> {
    return _errorMessages
  }

  private fun loadSurveys() {
    surveysRepository.fetchSurveysFromApi()
      .subscribeOn(ioScheduler)
      .observeOn(mainScheduler)
      .subscribe({
        _surveys.value = it
      }, {
        _errorMessages.value = "${it.javaClass.simpleName}(${it.message})"
      }).let {
        // Ignore
        // We don't need to manually unsubscribe this observer,
        // since the observer only modifies the LiveData, which already takes the Activity's life cycle into account
      }
  }
}

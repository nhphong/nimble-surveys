package com.nhphong.nimblesurveys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.data.SurveysRepository
import com.nhphong.nimblesurveys.di.IOScheduler
import com.nhphong.nimblesurveys.di.MainScheduler
import com.nhphong.nimblesurveys.utils.Event
import io.reactivex.Scheduler
import javax.inject.Inject

abstract class SurveysViewModel : ViewModel() {
  abstract val surveys: LiveData<List<Survey>>
  abstract val errorMessages: LiveData<Event<String>>
  abstract val openSurveyEvent: LiveData<Event<String>>

  abstract fun openSurvey(surveyId: String)
}

class SurveysViewModelImpl @Inject constructor(
  private val surveysRepository: SurveysRepository,
  @MainScheduler private val mainScheduler: Scheduler,
  @IOScheduler private val ioScheduler: Scheduler
) : SurveysViewModel() {

  override val surveys by lazy {
    MutableLiveData<List<Survey>>().also {
      loadSurveys()
    }
  }

  override val errorMessages by lazy {
    MutableLiveData<Event<String>>()
  }

  override val openSurveyEvent by lazy {
    MutableLiveData<Event<String>>()
  }

  override fun openSurvey(surveyId: String) {
    openSurveyEvent.value = Event(surveyId)
  }

  private fun loadSurveys() {
    surveysRepository.fetchSurveysFromApi()
      .subscribeOn(ioScheduler)
      .observeOn(mainScheduler)
      .subscribe({
        surveys.value = it
      }, {
        errorMessages.value = Event("${it.javaClass.simpleName}(${it.message})")
      }).let {
        // Ignore
        // We don't need to manually unsubscribe this observer,
        // since the observer only modifies the LiveData, which already takes the Activity's life cycle into account
      }
  }
}

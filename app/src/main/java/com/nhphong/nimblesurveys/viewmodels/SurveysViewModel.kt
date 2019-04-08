package com.nhphong.nimblesurveys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.data.SurveysRepository
import com.nhphong.nimblesurveys.di.IOScheduler
import com.nhphong.nimblesurveys.di.MainScheduler
import com.nhphong.nimblesurveys.utils.Event
import com.nhphong.nimblesurveys.utils.fullMesssage
import io.reactivex.Scheduler
import javax.inject.Inject

abstract class SurveysViewModel : ViewModel() {
  abstract val surveys: LiveData<List<Survey>>
  abstract val errorMessage: LiveData<Event<String>>
  abstract val internalErrorMessage: LiveData<Event<String>>
  abstract val openSurveyEvent: LiveData<Event<String>>

  abstract fun openSurvey(surveyId: String)
  abstract fun loadSurveys()
}

class SurveysViewModelImpl @Inject constructor(
  private val surveysRepository: SurveysRepository,
  @MainScheduler private val mainScheduler: Scheduler,
  @IOScheduler private val ioScheduler: Scheduler
) : SurveysViewModel() {

  override val surveys = MutableLiveData<List<Survey>>()
  override val errorMessage = MutableLiveData<Event<String>>()
  override val internalErrorMessage = MutableLiveData<Event<String>>()
  override val openSurveyEvent = MutableLiveData<Event<String>>()

  override fun openSurvey(surveyId: String) {
    openSurveyEvent.value = Event(surveyId)
  }

  override fun loadSurveys() {
    // Step 1: Load surveys from database, so user won't have to wait
    surveysRepository.loadSurveysFromDB()
      .subscribeOn(ioScheduler)
      .observeOn(mainScheduler)
      .subscribe({
        surveys.value = it
      }, {
        errorMessage.value = Event(it.fullMesssage())
      }).let {
        // Ignored
        // We don't need to manually unsubscribe this observer,
        // since the observer only modifies the LiveData, which already takes the Activity's life cycle into account
      }

    // Step 2: Fetch surveys from remote server
    surveysRepository.fetchSurveysFromApi()
      .subscribeOn(ioScheduler)
      .observeOn(mainScheduler)
      .doOnSuccess {
        // Step 3: Save surveys to database for caching
        surveysRepository.saveSurveysToDB(it)
          .subscribeOn(ioScheduler)
          .observeOn(mainScheduler)
          .subscribe({
            // Ignored
          }, { err ->
            internalErrorMessage.value = Event(err.fullMesssage())
          }).let {
            // Ignored
          }
      }
      .subscribe({
        surveys.value = it
      }, {
        errorMessage.value = Event(it.fullMesssage())
      }).let {
        // Ignored
      }
  }
}

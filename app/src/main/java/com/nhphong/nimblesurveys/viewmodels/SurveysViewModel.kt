package com.nhphong.nimblesurveys.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.data.SurveysRepository
import com.nhphong.nimblesurveys.di.IOScheduler
import com.nhphong.nimblesurveys.di.MainScheduler
import com.nhphong.nimblesurveys.utils.Event
import com.nhphong.nimblesurveys.utils.StringResProvider
import com.nhphong.nimblesurveys.utils.fullMessage
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class SurveysViewModel : ViewModel() {
  abstract val surveys: LiveData<List<Survey>>
  abstract val message: LiveData<String>
  abstract val snackBarMessage: LiveData<Event<String>>
  abstract val internalErrorMessage: LiveData<Event<String>>
  abstract val openSurveyEvent: LiveData<Event<String>>

  abstract fun openSurvey(surveyId: String)
  abstract fun loadSurveys()
  abstract fun reloadSurveys()

  @VisibleForTesting
  abstract val disposables: CompositeDisposable
}

class SurveysViewModelImpl @Inject constructor(
  private val surveysRepository: SurveysRepository,
  private val stringResProvider: StringResProvider,
  @MainScheduler private val mainScheduler: Scheduler,
  @IOScheduler private val ioScheduler: Scheduler
) : SurveysViewModel() {

  override val surveys = MutableLiveData<List<Survey>>()
  override val message = MutableLiveData<String>()
  override val snackBarMessage = MutableLiveData<Event<String>>()
  override val internalErrorMessage = MutableLiveData<Event<String>>()
  override val openSurveyEvent = MutableLiveData<Event<String>>()
  override val disposables = CompositeDisposable()

  override fun openSurvey(surveyId: String) {
    openSurveyEvent.value = Event(surveyId)
  }

  override fun loadSurveys() {
    // Step 1: Load surveys from database, so user won't have to wait
    loadSurveysFromDB()

    // Step 2: Fetch surveys from remote server
    fetchSurveysFromRemoteServer()
  }

  override fun reloadSurveys() {
    snackBarMessage.value = Event(stringResProvider.fetchingSurveys)
    fetchSurveysFromRemoteServer()
  }

  override fun onCleared() {
    disposables.clear()
  }

  private fun loadSurveysFromDB() {
    surveysRepository.loadSurveysFromDB()
      .subscribeOn(ioScheduler)
      .observeOn(mainScheduler)
      .subscribe({
        surveys.value = it
      }, {
        snackBarMessage.value = Event(it.fullMessage())
      })
      .let {
        disposables.add(it)
      }
  }

  private fun fetchSurveysFromRemoteServer() {
    message.value = stringResProvider.fetchingSurveys
    surveysRepository.fetchSurveysFromApi()
      .subscribeOn(ioScheduler)
      .observeOn(mainScheduler)
      .doOnSuccess {
        // Step 3: Save surveys to database for caching
        saveSurveysToDB(it)
      }
      .subscribe({
        surveys.value = it
        message.value = if (it.isEmpty()) stringResProvider.noSurveysMessage else ""
      }, {
        message.value = it.fullMessage()
        snackBarMessage.value = Event(it.fullMessage())
      })
      .let {
        disposables.add(it)
      }
  }

  private fun saveSurveysToDB(surveys: List<Survey>) {
    surveysRepository.saveSurveysToDB(surveys)
      .subscribeOn(ioScheduler)
      .observeOn(mainScheduler)
      .subscribe({
        // Ignored
      }, { err ->
        internalErrorMessage.value = Event(err.fullMessage())
      })
      .let {
        disposables.add(it)
      }
  }
}

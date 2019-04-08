package com.nhphong.nimblesurveys.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.data.SurveysRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SurveysViewModelImplTest {

  private lateinit var surveysRepository: SurveysRepository
  private lateinit var viewModel: SurveysViewModel

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setup() {
    surveysRepository = mock {
      on { loadSurveysFromDB() }.thenReturn(Single.just(surveysFromDb))
      on { fetchSurveysFromApi() }.thenReturn(Single.just(surveysFromApi))
      on { saveSurveysToDB(any()) }.thenReturn(Completable.complete())
    }

    val immediateScheduler = Schedulers.from { it.run() }
    viewModel = SurveysViewModelImpl(surveysRepository, immediateScheduler, immediateScheduler)
  }

  @Test
  fun openSurvey() {
    val observer = viewModel.openSurveyEvent.test()
    viewModel.openSurvey("abc")

    observer.assertValue {
      it.peekContent() == "abc"
    }
  }

  @Test
  fun loadSurveys_happyCase() {
    val observer = viewModel.surveys.test()
    viewModel.loadSurveys()
    verify(surveysRepository).loadSurveysFromDB()
    verify(surveysRepository).fetchSurveysFromApi()
    verify(surveysRepository).saveSurveysToDB(surveysFromApi)

    observer
      .assertHistorySize(2)
      .assertValueHistory(surveysFromDb, surveysFromApi)
  }

  private companion object TestData {
    val surveysFromDb = List(2) { Survey() }
    val surveysFromApi = List(3) { Survey() }
  }
}

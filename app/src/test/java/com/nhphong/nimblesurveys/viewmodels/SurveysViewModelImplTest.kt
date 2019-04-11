package com.nhphong.nimblesurveys.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.nhaarman.mockito_kotlin.*
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.data.SurveysRepository
import com.nhphong.nimblesurveys.utils.StringResProvider
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class SurveysViewModelImplTest {

  private lateinit var surveysRepository: SurveysRepository
  private lateinit var stringResProvider: StringResProvider
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

    stringResProvider = mock {
      on { fetchingSurveys }.thenReturn("Fetching surveys...")
      on { noSurveysMessage }.thenReturn("There are no surveys")
    }

    val immediateScheduler = Schedulers.from { it.run() }
    viewModel = SurveysViewModelImpl(
      surveysRepository,
      stringResProvider,
      immediateScheduler,
      immediateScheduler
    )
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
  fun loadSurveys_HappyCase() {
    val surveyObserver = viewModel.surveys.test()
    val messageObserver = viewModel.message.test()

    viewModel.loadSurveys()
    verify(surveysRepository).loadSurveysFromDB()
    verify(surveysRepository).fetchSurveysFromApi()
    verify(surveysRepository).saveSurveysToDB(surveysFromApi)
    assertEquals(3, viewModel.disposables.size())

    surveyObserver
      .assertHistorySize(2)
      .assertValueHistory(surveysFromDb, surveysFromApi)

    messageObserver
      .assertHistorySize(2)
      .assertValueHistory("Fetching surveys...", "")
  }

  @Test
  fun loadSurveys_ThereIsNoCacheInTheDB() {
    doReturn(Single.just(emptyList<Survey>()))
      .whenever(surveysRepository).loadSurveysFromDB()

    val surveysObserver = viewModel.surveys.test()
    val messageObserver = viewModel.message.test()

    viewModel.loadSurveys()
    verify(surveysRepository).loadSurveysFromDB()
    verify(surveysRepository).fetchSurveysFromApi()
    verify(surveysRepository).saveSurveysToDB(surveysFromApi)
    assertEquals(3, viewModel.disposables.size())

    surveysObserver
      .assertHistorySize(2)
      .assertValueHistory(emptyList(), surveysFromApi)

    messageObserver
      .assertHistorySize(2)
      .assertValueHistory("Fetching surveys...", "")
  }

  @Test
  fun loadSurveys_CacheWasFound_ButApiFailed() {
    val error = Exception("Server is down")
    doReturn(Single.error<List<Survey>>(error))
      .whenever(surveysRepository).fetchSurveysFromApi()

    val surveysObserver = viewModel.surveys.test()
    val messageObserver = viewModel.message.test()
    val snackBarMessageObserver = viewModel.snackBarMessage.test()

    viewModel.loadSurveys()
    verify(surveysRepository).loadSurveysFromDB()
    verify(surveysRepository).fetchSurveysFromApi()
    verifyNoMoreInteractions(surveysRepository) // No database caching was performed
    assertEquals(2, viewModel.disposables.size())

    surveysObserver
      .assertHistorySize(1)
      .assertValueHistory(surveysFromDb)

    messageObserver
      .assertHistorySize(2)
      .assertValueHistory("Fetching surveys...", "Exception (Server is down)")

    snackBarMessageObserver
      .assertValue {
        it.peekContent() == "Exception (Server is down)"
      }
  }

  @Test
  fun loadSurveys_ErrorOccursWhenLoadingSurveysFromDB() {
    val error = IOException("Unable to access database")
    doReturn(Single.error<List<Survey>>(error))
      .whenever(surveysRepository).loadSurveysFromDB()

    val surveysObserver = viewModel.surveys.test()
    val messageObserver = viewModel.message.test()
    val snackBarMessageObserver = viewModel.snackBarMessage.test()

    viewModel.loadSurveys()
    verify(surveysRepository).loadSurveysFromDB()
    verify(surveysRepository).fetchSurveysFromApi()
    verify(surveysRepository).saveSurveysToDB(surveysFromApi)
    assertEquals(3, viewModel.disposables.size())

    surveysObserver
      .assertHistorySize(1)
      .assertValueHistory(surveysFromApi)

    messageObserver
      .assertHistorySize(2)
      .assertValueHistory("Fetching surveys...", "")

    snackBarMessageObserver
      .assertValue {
        it.peekContent() == "IOException (Unable to access database)"
      }
  }

  @Test
  fun loadSurveys_FetchSurveysFromApiReturnsNothing() {
    doReturn(Single.just(emptyList<Survey>()))
      .whenever(surveysRepository).fetchSurveysFromApi()

    val surveysObserver = viewModel.surveys.test()
    val messageObserver = viewModel.message.test()

    viewModel.loadSurveys()
    verify(surveysRepository).loadSurveysFromDB()
    verify(surveysRepository).fetchSurveysFromApi()
    verify(surveysRepository).saveSurveysToDB(emptyList())
    assertEquals(3, viewModel.disposables.size())

    surveysObserver
      .assertHistorySize(2)
      .assertValueHistory(surveysFromDb, emptyList())

    messageObserver
      .assertHistorySize(2)
      .assertValueHistory("Fetching surveys...", "There are no surveys")
  }

  @Test
  fun loadSurveys_ErrorOccursWhenCachingResultFromApi() {
    val error = IOException("Unable to access database")
    doReturn(Completable.error(error))
      .whenever(surveysRepository).saveSurveysToDB(any())

    val surveysObserver = viewModel.surveys.test()
    val messageObserver = viewModel.message.test()
    val internalErrorMessageObserver = viewModel.internalErrorMessage.test()

    viewModel.loadSurveys()
    verify(surveysRepository).loadSurveysFromDB()
    verify(surveysRepository).fetchSurveysFromApi()
    verify(surveysRepository).saveSurveysToDB(surveysFromApi)
    assertEquals(3, viewModel.disposables.size())

    surveysObserver
      .assertHistorySize(2)
      .assertValueHistory(surveysFromDb, surveysFromApi)

    messageObserver
      .assertHistorySize(2)
      .assertValueHistory("Fetching surveys...", "")

    internalErrorMessageObserver
      .assertValue {
        it.peekContent() == "IOException (Unable to access database)"
      }
  }

  @Test
  fun reloadSurveys_HappyCase() {
    val snackBarMessageObserver = viewModel.snackBarMessage.test()
    val messageObserver = viewModel.message.test()
    val surveysObserver = viewModel.surveys.test()

    viewModel.reloadSurveys()
    verify(surveysRepository).fetchSurveysFromApi()
    verify(surveysRepository).saveSurveysToDB(surveysFromApi)
    assertEquals(2, viewModel.disposables.size())

    snackBarMessageObserver
      .assertValue {
        it.peekContent() == "Fetching surveys..."
      }

    messageObserver
      .assertHistorySize(2)
      .assertValueHistory("Fetching surveys...", "")

    surveysObserver
      .assertValue(surveysFromApi)
  }

  private companion object TestData {
    val surveysFromDb = List(2) { Survey() }
    val surveysFromApi = List(3) { Survey() }
  }
}

package com.nhphong.nimblesurveys.views.activity

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.*
import com.nhaarman.mockito_kotlin.mock
import com.nhphong.nimblesurveys.R
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.matchers.viewPagerWithId
import com.nhphong.nimblesurveys.rules.InjectedActivityTestRule
import com.nhphong.nimblesurveys.utils.Event
import com.nhphong.nimblesurveys.viewmodels.SurveysViewModel
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

  private val surveys = MutableLiveData<List<Survey>>()
  private val message = MutableLiveData<String>()
  private val errorMessage = MutableLiveData<Event<String>>()
  private val internalErrorMessage = MutableLiveData<Event<String>>()
  private val openSurveyEvent = MutableLiveData<Event<String>>()

  private val viewModel = mock<SurveysViewModel> {
    on { surveys }.thenReturn(surveys)
    on { message }.thenReturn(message)
    on { errorMessage }.thenReturn(errorMessage)
    on { internalErrorMessage }.thenReturn(internalErrorMessage)
    on { openSurveyEvent }.thenReturn(openSurveyEvent)
  }

  @get:Rule
  val rule = InjectedActivityTestRule(MainActivity::class.java) {
    it.surveysViewModel = viewModel
  }

  @Before
  fun setup() {
    surveys.postValue(surveysTestData)
  }

  @Test
  fun displayUIProperly() {
    val viewPager = viewPagerWithId(R.id.view_pager)
    val currentPage = allOf(withParent(viewPager), isDisplayed())

    onView(
      allOf(
        withParent(currentPage),
        withId(R.id.name)
      )
    ).check(matches(withText("Octave")))


    onView(
      allOf(
        withParent(currentPage),
        withId(R.id.description)
      )
    ).check(matches(withText(containsString("Marriott Bangkok"))))

    onView(
      allOf(
        withParent(currentPage),
        withId(R.id.take_survey_btn)
      )
    ).check(matches(withText("Take the survey")))

    onView(isRoot()).perform(swipeLeft(), swipeLeft(), swipeRight())
    onView(withText("East Agile")).check(matches(isDisplayed()))
  }

  @Test
  fun displayErrorMessage() {
    errorMessage.postValue(Event("An unexpected error has occurred"))
    onView(
      allOf(
        withId(com.google.android.material.R.id.snackbar_text),
        withText("An unexpected error has occurred")
      )
    ).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
  }

  @Test
  fun clickOnTakeTheSurveyButton() {
    onView(isRoot()).perform(swipeLeft())
    openSurveyEvent.postValue(Event("2"))
    Intents.intended(
      allOf(
        hasComponent(SurveyDetailActivity::class.java.name),
        hasExtra(SurveyDetailActivity.EXTRA_SURVEY_ID, "2")
      )
    )
    onView(withText("Details for survey\n#2")).check(matches(isDisplayed()))
  }

  @Test
  fun noSurveysMessage() {
    message.postValue("Oops! There are no surveys")
    surveys.postValue(emptyList())
    onView(withText("Oops! There are no surveys"))
      .check(matches(isDisplayed()))
  }

  private companion object TestData {
    val surveysTestData = listOf(
      Survey(
        "1",
        "Octave",
        "Marriott Bangkok Sukhumvit Hotel"
      ),
      Survey(
        "2",
        "East Agile",
        "Vietnam's first and purest agile software development firm"
      ),
      Survey(
        "3",
        "Grab",
        "Vietnam R&D Lab at Grab focuses on building and nurturing the best engineering talents from diverse academic backgrounds"
      )
    )
  }
}

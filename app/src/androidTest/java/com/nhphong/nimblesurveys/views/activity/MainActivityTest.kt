package com.nhphong.nimblesurveys.views.activity

import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhphong.nimblesurveys.R
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.helpers.waitUntil
import com.nhphong.nimblesurveys.matchers.recyclerViewHasItemCount
import com.nhphong.nimblesurveys.matchers.recyclerViewWithId
import com.nhphong.nimblesurveys.matchers.viewPagerWithId
import com.nhphong.nimblesurveys.rules.InjectedActivityTestRule
import com.nhphong.nimblesurveys.utils.Event
import com.nhphong.nimblesurveys.viewmodels.SurveysViewModel
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Callable

class MainActivityTest {

  private val surveys = MutableLiveData<List<Survey>>()
  private val message = MutableLiveData<String>()
  private val snackBarMessage = MutableLiveData<Event<String>>()
  private val internalErrorMessage = MutableLiveData<Event<String>>()
  private val openSurveyEvent = MutableLiveData<Event<String>>()

  private val viewModel = mock<SurveysViewModel> {
    on { surveys }.thenReturn(surveys)
    on { message }.thenReturn(message)
    on { snackBarMessage }.thenReturn(snackBarMessage)
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
  fun displaySurveysProperly() {
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

    onView(viewPager).perform(swipeUp())

    waitUntil("the page finished scrolling", Callable {
      onView(withText("East Agile")).check(matches(isDisplayed()))
      true
    }, 1500)

    onView(viewPager).perform(swipeUp())

    waitUntil("the page finished scrolling", Callable {
      onView(withText("Grab")).check(matches(isDisplayed()))
      true
    }, 1500)
  }

  @Test
  fun displayToolbarProperly() {
    onView(
      allOf(
        isAssignableFrom(TextView::class.java),
        withParent(withId(R.id.toolbar))
      )
    ).check(matches(withText("SURVEYS")))

    onView(
      allOf(
        withId(R.id.reload_button),
        withParent(withId(R.id.toolbar))
      )
    ).check(matches(isDisplayed()))

    onView(
      allOf(
        withId(R.id.menu_button),
        withParent(withId(R.id.toolbar))
      )
    ).check(matches(isDisplayed()))
  }

  @Test
  fun displayIndicatorListProperly() {
    val bullets = recyclerViewWithId(R.id.bullets)
    onView(bullets).check(recyclerViewHasItemCount(3))

    // First bullet is selected
    onView(allOf(withParent(bullets), withParentIndex(0)))
      .check(matches(isSelected()))
    // Second and third bullets are NOT selected
    onView(allOf(withParent(bullets), withParentIndex(1)))
      .check(matches(not(isSelected())))
    onView(allOf(withParent(bullets), withParentIndex(2)))
      .check(matches(not(isSelected())))

    // Scroll to the third page
    onView(viewPagerWithId(R.id.view_pager)).perform(swipeUp(), swipeUp())
    waitUntil("the page finished scrolling", Callable {
      onView(withText("Grab")).check(matches(isDisplayed()))
      true
    }, 2000)

    // First and second bullets are now NOT selected
    onView(allOf(withParent(bullets), withParentIndex(0)))
      .check(matches(not(isSelected())))
    onView(allOf(withParent(bullets), withParentIndex(1)))
      .check(matches(not(isSelected())))

    // Third bullet is selected
    onView(allOf(withParent(bullets), withParentIndex(2)))
      .check(matches(isSelected()))
  }

  @Test
  fun displaySnackBarMessage() {
    snackBarMessage.postValue(Event("An unexpected error has occurred"))
    onView(
      allOf(
        withId(R.id.snackbar_text),
        withText("An unexpected error has occurred")
      )
    ).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
  }

  @Test
  fun noSurveysMessage() {
    message.postValue("Oops! There are no surveys")
    surveys.postValue(emptyList())
    onView(withText("Oops! There are no surveys"))
      .check(matches(isDisplayed()))
  }

  @Test
  fun clickOnReloadButton() {
    onView(
      allOf(
        withId(R.id.reload_button),
        withParent(withId(R.id.toolbar))
      )
    ).perform(click())

    verify(viewModel).reloadSurveys()
  }

  @Test
  fun clickOnTakeTheSurveyButton() {
    onView(viewPagerWithId(R.id.view_pager)).perform(swipeUp())
    openSurveyEvent.postValue(Event("2"))
    Intents.intended(
      allOf(
        hasComponent(SurveyDetailActivity::class.java.name),
        hasExtra(SurveyDetailActivity.EXTRA_SURVEY_ID, "2")
      )
    )
    onView(withText("Details for survey\n#2")).check(matches(isDisplayed()))
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

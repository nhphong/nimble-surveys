package com.nhphong.nimblesurveys.matchers

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.viewpager.widget.ViewPager
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

fun viewPagerWithId(@IdRes id: Int): Matcher<View> {
  return allOf(isAssignableFrom(ViewPager::class.java), withId(id), isDisplayed())
}



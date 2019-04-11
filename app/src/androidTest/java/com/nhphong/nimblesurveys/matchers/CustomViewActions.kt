package com.nhphong.nimblesurveys.matchers

import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe

fun customSwipeUp(): ViewAction {
  return GeneralSwipeAction(
    Swipe.SLOW,
    GeneralLocation.BOTTOM_CENTER,
    GeneralLocation.TOP_CENTER,
    Press.FINGER
  )
}

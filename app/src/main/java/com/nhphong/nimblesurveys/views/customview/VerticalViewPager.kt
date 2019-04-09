package com.nhphong.nimblesurveys.views.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager


class VerticalViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

  init {
    setPageTransformer(true, VerticalPageTransformer())
    overScrollMode = OVER_SCROLL_NEVER
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    return super.onTouchEvent(swapXY(ev))
  }

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    return super.onInterceptTouchEvent(swapXY(ev)).also {
      swapXY(ev) // swap x,y back for other touch events
    }
  }

  private fun swapXY(ev: MotionEvent): MotionEvent {
    return ev.apply {
      val newX = (ev.y / height) * width
      val newY = (ev.x / width) * height
      setLocation(newX, newY)
    }
  }

  class VerticalPageTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
      when {
        position < -1 -> {
          // [-infinity, -1], view page is off-screen to the left
          // hide the page.
          page.visibility = INVISIBLE
        }
        position <= 1 -> {
          // [-1, 1], page is on screen
          // show the page
          page.visibility = VISIBLE

          // get page back to the center of screen since it will get swipe horizontally by default.
          page.translationX = page.width * -position

          // set Y position to swipe in vertical direction.
          val y = position * page.height
          page.translationY = y
        }
        else -> {
          // [1, +infinity], page is off-screen to the right
          // hide the page.
          page.visibility = INVISIBLE
        }
      }
    }
  }
}

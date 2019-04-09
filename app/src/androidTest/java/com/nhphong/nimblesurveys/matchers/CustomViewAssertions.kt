package com.nhphong.nimblesurveys.matchers

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion

fun recyclerViewHasItemCount(count: Int): ViewAssertion {
  return ViewAssertion { view, _ ->
    view is RecyclerView && view.adapter?.itemCount == count
  }
}



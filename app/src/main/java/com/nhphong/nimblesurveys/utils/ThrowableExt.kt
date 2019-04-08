package com.nhphong.nimblesurveys.utils

fun Throwable.fullMesssage(): String {
  return "${this.javaClass.simpleName}(${this.message})"
}

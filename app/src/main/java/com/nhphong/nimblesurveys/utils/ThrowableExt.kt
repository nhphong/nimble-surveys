package com.nhphong.nimblesurveys.utils

fun Throwable.fullMessage(): String {
  return "${this.javaClass.simpleName} (${this.message})"
}

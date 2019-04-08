package com.nhphong.nimblesurveys.di

import javax.inject.Qualifier

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class IOScheduler

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class MainScheduler

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class WithInterceptor

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class WithoutInterceptor

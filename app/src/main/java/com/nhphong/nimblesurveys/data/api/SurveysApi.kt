package com.nhphong.nimblesurveys.data.api

import com.nhphong.nimblesurveys.data.Survey
import io.reactivex.Observable
import retrofit2.http.GET

interface SurveysApi {
  @GET("surveys.json")
  fun getSurveys(): Observable<List<Survey>>
}

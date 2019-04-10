package com.nhphong.nimblesurveys.data.api

import com.nhphong.nimblesurveys.data.Survey
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface SurveysApi {
  @GET("surveys.json")
  fun getSurveys(
    @Query("page") page: Int = 1,
    @Query("per_page") perPage: Int = 10
  ): Observable<List<Survey>>
}

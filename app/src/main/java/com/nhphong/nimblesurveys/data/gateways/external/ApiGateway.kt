package com.nhphong.nimblesurveys.data.gateways.external

import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.data.api.SurveysApi
import io.reactivex.Single
import javax.inject.Inject

interface ApiGateway {
  fun fetchSurveys(): Single<List<Survey>>
}

class ApiGatewayImpl @Inject constructor(
  private val surveysApi: SurveysApi
) : ApiGateway {
  override fun fetchSurveys(): Single<List<Survey>> {
    return surveysApi.getSurveys().firstOrError()
  }
}

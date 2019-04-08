package com.nhphong.nimblesurveys.data

import com.nhphong.nimblesurveys.data.gateways.external.ApiGateway
import com.nhphong.nimblesurveys.data.gateways.local.DatabaseGateway
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

interface SurveysRepository {
  fun loadSurveysFromDB(): Single<List<Survey>>
  fun saveSurveysToDB(surveys: List<Survey>): Completable
  fun fetchSurveysFromApi(): Single<List<Survey>>
}

class SurveysRepositoryImpl @Inject constructor(
  private val databaseGateway: DatabaseGateway,
  private val apiGateway: ApiGateway
) : SurveysRepository {

  override fun loadSurveysFromDB(): Single<List<Survey>> {
    return databaseGateway.loadSurveys()
  }

  override fun saveSurveysToDB(surveys: List<Survey>): Completable {
    return databaseGateway.refreshSurveys(surveys)
  }

  override fun fetchSurveysFromApi(): Single<List<Survey>> {
    return apiGateway.fetchSurveys()
  }
}

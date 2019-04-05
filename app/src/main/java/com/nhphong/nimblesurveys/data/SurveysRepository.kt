package com.nhphong.nimblesurveys.data

import com.nhphong.nimblesurveys.data.gateways.local.DatabaseGateway
import io.reactivex.Single
import javax.inject.Inject

interface SurveysRepository {
  fun getSurveys(): Single<List<Survey>>
}

class SurveysRepositoryImpl @Inject constructor(
  private val databaseGateway: DatabaseGateway
) : SurveysRepository {
  override fun getSurveys(): Single<List<Survey>> {
    return databaseGateway.getSurveys()
  }
}

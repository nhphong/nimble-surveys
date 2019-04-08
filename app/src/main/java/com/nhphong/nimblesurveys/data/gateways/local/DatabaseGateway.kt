package com.nhphong.nimblesurveys.data.gateways.local

import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.data.db.AppDatabase
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

interface DatabaseGateway {
  fun insertSurveys(surveys: List<Survey>): Completable
  fun loadSurveys(): Single<List<Survey>>
  fun deleteAllSurveys(): Completable
  fun refreshSurveys(surveys: List<Survey>): Completable
}

class DatabaseGatewayImpl @Inject constructor(
  private val database: AppDatabase
) : DatabaseGateway {
  override fun insertSurveys(surveys: List<Survey>): Completable {
    return database.surveyDao().insertAll(surveys)
  }

  override fun loadSurveys(): Single<List<Survey>> {
    return database.surveyDao().loadAll().toSingle(emptyList())
  }

  override fun deleteAllSurveys(): Completable {
    return database.surveyDao().deleteAll()
  }

  override fun refreshSurveys(surveys: List<Survey>): Completable {
    return deleteAllSurveys().andThen(insertSurveys(surveys))
  }
}

package com.nhphong.nimblesurveys.data.gateways.local

import com.nhphong.nimblesurveys.data.Survey
import io.reactivex.Single
import javax.inject.Inject

interface DatabaseGateway {
  fun loadSurveys(): Single<List<Survey>>
}

class DatabaseGatewayImpl @Inject constructor() : DatabaseGateway {
  override fun loadSurveys(): Single<List<Survey>> {
    return Single.just(emptyList())
  }
}

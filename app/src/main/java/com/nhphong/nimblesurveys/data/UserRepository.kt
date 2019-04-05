package com.nhphong.nimblesurveys.data

import io.reactivex.Single
import javax.inject.Inject

interface UserRepository {
  fun getAccessToken(): Single<AccessToken>
}

class UserRepositoryImpl @Inject constructor() : UserRepository {
  override fun getAccessToken(): Single<AccessToken> {
    return Single.just(AccessToken("d9584af77d8c0d6622e2b3c554ed520b2ae64ba0721e52daa12d6eaa5e5cdd93"))
  }
}

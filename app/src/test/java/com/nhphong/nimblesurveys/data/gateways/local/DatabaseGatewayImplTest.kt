package com.nhphong.nimblesurveys.data.gateways.local

import androidx.room.Room
import com.nhphong.nimblesurveys.data.Survey
import com.nhphong.nimblesurveys.data.db.AppDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.application
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23], manifest = Config.NONE)
class DatabaseGatewayImplTest {

  private lateinit var database: AppDatabase
  private lateinit var gateway: DatabaseGateway

  @Before
  fun setup() {
    database = Room.inMemoryDatabaseBuilder(
      application,
      AppDatabase::class.java
    ).allowMainThreadQueries().build()

    gateway = DatabaseGatewayImpl(database)
  }

  @After
  fun tearDown() {
    database.close()
  }

  @Test
  fun insertSurveys() {
    gateway.insertSurveys(surveys)
      .test()
      .assertComplete()

    database.surveyDao()
      .loadSurveyById("2")
      .test()
      .assertValue(surveys[1])
  }

  @Test
  fun loadSurveys() {
    // Empty database
    gateway.loadSurveys()
      .test()
      .assertValue(emptyList())

    // Insert some data
    gateway.insertSurveys(surveys)
      .test()
      .await()

    gateway.loadSurveys()
      .test()
      .awaitCount(1)
      .assertValue(surveys)
  }

  @Test
  fun deleteAllSurveys() {
    // Insert some data, then delete
    gateway.insertSurveys(surveys)
      .test()
      .await()

    gateway.loadSurveys()
      .test()
      .awaitCount(1)
      .assertValue(surveys)

    gateway.deleteAllSurveys()
      .test()
      .await()

    gateway.loadSurveys()
      .test()
      .assertValue(emptyList())
  }

  @Test
  fun refreshSurveys() {
    gateway.insertSurveys(surveys)
      .test()
      .await()
    gateway.loadSurveys()
      .test()
      .assertValue { it.size == 5 }

    gateway.refreshSurveys(listOf(Survey()))
      .test()
      .await()
    gateway.loadSurveys()
      .test()
      .assertValue { it.size == 1 }
  }

  private companion object TestData {
    val surveys = listOf("1", "2", "3", "4", "5").map {
      Survey(
        it,
        "name #$it",
        "description #$it",
        "cover image url #$it"
      )
    }
  }
}

package com.nhphong.nimblesurveys.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nhphong.nimblesurveys.data.Survey

@Database(entities = [Survey::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun surveyDao(): SurveyDAO

  companion object {
    const val DATABASE_NAME = "Nimble Surveys Database"
  }
}

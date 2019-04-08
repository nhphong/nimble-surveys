package com.nhphong.nimblesurveys.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nhphong.nimblesurveys.data.Survey
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface SurveyDAO {
  @Query("SELECT * FROM survey")
  fun loadAll(): Maybe<List<Survey>>

  @Insert
  fun insertAll(surveys: List<Survey>): Completable

  @Query("DELETE FROM survey")
  fun deleteAll(): Completable

  @Query("SELECT * FROM survey WHERE id = :surveyId")
  fun loadSurveyById(surveyId: String): Maybe<Survey>
}

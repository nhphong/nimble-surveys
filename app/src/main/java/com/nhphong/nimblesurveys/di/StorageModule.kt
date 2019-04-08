package com.nhphong.nimblesurveys.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.nhphong.nimblesurveys.data.db.AppDatabase
import com.nhphong.nimblesurveys.data.db.AppDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {
  @Provides
  @Singleton
  fun sharedPreferences(@ApplicationContext applicationContext: Context): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(applicationContext)
  }

  @Provides
  @Singleton
  fun database(@ApplicationContext context: Context): AppDatabase {
    return Room.databaseBuilder(
      context, AppDatabase::class.java, DATABASE_NAME
    ).build()
  }
}

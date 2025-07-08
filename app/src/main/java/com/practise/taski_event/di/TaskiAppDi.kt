package com.practise.taski_event.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practise.taski_event.data.AppDatabase
import com.practise.taski_event.data.EventDao
import com.practise.taski_event.repo.AppDatabaseRepo
import com.practise.taski_event.repo.AppDatabaseRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskiAppDi {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "event_database"
        ).build()
    }

    @Provides
    fun provideEventDao(db: AppDatabase): EventDao = db.eventDao()

    @Provides
    fun provideEventRepository(dao: EventDao): AppDatabaseRepo = AppDatabaseRepoImpl(dao)

}
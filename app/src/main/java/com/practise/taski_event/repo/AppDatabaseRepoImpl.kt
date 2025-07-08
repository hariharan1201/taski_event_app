package com.practise.taski_event.repo

import com.practise.taski_event.data.Event
import com.practise.taski_event.data.EventDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AppDatabaseRepoImpl @Inject constructor(private val appDatabase: EventDao): AppDatabaseRepo {
    override fun addEvent(event: Event): Flow<AppState> = flow {
        emit(AppState.Loading)
        appDatabase.insertEvent(event)
        emit(AppState.Success)
    }.flowOn(Dispatchers.IO)

    override fun deleteEvent(event: Event): Flow<AppState> = flow {
        emit(AppState.Loading)
        appDatabase.deleteEvent(event)
        emit(AppState.Deleted)
    }.flowOn(Dispatchers.IO)

    override fun getEvent(): Flow<AppState> = flow {
        emit(AppState.Loading)
        val events = appDatabase.getAllEvents()
        emit(AppState.Events(events))
    }.flowOn(Dispatchers.IO)

    override fun getEventByDate(): Flow<AppState> = flow {
        emit(AppState.Loading)
    }.flowOn(Dispatchers.IO)

    override fun updateEvent(event: Event): Flow<AppState> = flow<AppState> {
        emit(AppState.Loading)
        appDatabase.updateEvent(event)
        emit(AppState.Success)
    }.flowOn(Dispatchers.IO)
}
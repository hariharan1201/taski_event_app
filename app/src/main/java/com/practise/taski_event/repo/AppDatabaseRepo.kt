package com.practise.taski_event.repo

import com.practise.taski_event.data.Event
import kotlinx.coroutines.flow.Flow

interface AppDatabaseRepo {
    fun addEvent(event: Event) : Flow<AppState>
    fun deleteEvent(event: Event) : Flow<AppState>
    fun getEvent(): Flow<AppState>
    fun getEventByDate() : Flow<AppState>
    fun updateEvent(event: Event): Flow<AppState>
}
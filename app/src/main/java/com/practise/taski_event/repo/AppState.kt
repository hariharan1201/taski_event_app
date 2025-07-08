package com.practise.taski_event.repo

import com.practise.taski_event.data.Event

sealed class AppState {
    data object Loading : AppState()
    data object Success : AppState()
    data object Failed : AppState()
    data object Deleted: AppState()
    data class Events(val list : List<Event>) : AppState()
}
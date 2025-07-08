package com.practise.taski_event.frags.allevent.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practise.taski_event.repo.AppState
import com.practise.taski_event.data.Event
import com.practise.taski_event.repo.AppDatabaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllEventViewModel @Inject constructor(private val appDatabaseRepo: AppDatabaseRepo): ViewModel() {
    private val _appState = MutableLiveData<AppState>()
    val appState = _appState

    fun getEvents() {
        viewModelScope.launch {
            appDatabaseRepo.getEvent().collectLatest { _appState.value = it }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            appDatabaseRepo.deleteEvent(event).collectLatest { _appState.value = it }
        }
    }

}
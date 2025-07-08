package com.practise.taski_event.frags.eventdialog.viewmodel

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
class AddEventViewModel @Inject constructor(private val appDatabaseRepo: AppDatabaseRepo): ViewModel() {
    private val _appState = MutableLiveData<AppState>()
    val appState = _appState

    fun addEvent(event: Event) {
        viewModelScope.launch {
            appDatabaseRepo.addEvent(event).collectLatest { _appState.value = it }
        }
    }

    fun updateEvent(event: Event){
        viewModelScope.launch {
            appDatabaseRepo.updateEvent(event).collectLatest { _appState.value = it }
        }
    }

}
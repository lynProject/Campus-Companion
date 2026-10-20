package com.inci.campuscompanion.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inci.campuscompanion.data.EventRepository
import com.inci.campuscompanion.model.CampusEvent
import kotlinx.coroutines.launch

sealed interface EventsUiState {
    object Loading : EventsUiState
    data class Success(val events: List<CampusEvent>, val endReached: Boolean) : EventsUiState
    data class Error(val message: String) : EventsUiState
}

class EventsViewModel(private val repository: EventRepository) : ViewModel() {

    var uiState: EventsUiState by mutableStateOf(EventsUiState.Loading)
        private set

    private var currentPage = 1
    private val pageSize = 5 // Number of items per page
    private val eventList = mutableListOf<CampusEvent>()
    var isPaginating by mutableStateOf(false)
        private set

    init {
        loadInitialEvents()
    }

    fun loadInitialEvents() {
        viewModelScope.launch {
            uiState = EventsUiState.Loading
            currentPage = 1
            eventList.clear()
            try {
                val newEvents = repository.fetchEvents(page = currentPage, limit = pageSize)
                eventList.addAll(newEvents)
                uiState = EventsUiState.Success(
                    events = eventList.toList(),
                    endReached = newEvents.size < pageSize
                )
            } catch (e: Exception) {
                uiState = EventsUiState.Error(e.localizedMessage ?: "Failed to load events")
            }
        }
    }

    fun loadNextPage() {
        if (isPaginating) return
        val currentState = uiState as? EventsUiState.Success ?: return
        if (currentState.endReached) return

        viewModelScope.launch {
            isPaginating = true
            currentPage++
            try {
                val newEvents = repository.fetchEvents(page = currentPage, limit = pageSize)
                if (newEvents.isNotEmpty()) {
                    eventList.addAll(newEvents)
                    uiState = EventsUiState.Success(
                        events = eventList.toList(),
                        endReached = newEvents.size < pageSize
                    )
                } else {
                    uiState = EventsUiState.Success(eventList.toList(), endReached = true)
                }
            } catch (e: Exception) {
                // Keep existing items even if pagination fails, or show toast
            } finally {
                isPaginating = false
            }
        }
    }
}
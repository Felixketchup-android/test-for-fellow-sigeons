package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Event
import com.example.data.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EventRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = EventRepository(database.eventDao())
    }

    // State for selected month and year
    private val _calendarState = MutableStateFlow(Calendar.getInstance())
    val calendarState: StateFlow<Calendar> = _calendarState.asStateFlow()

    // State for selected date (Format: YYYY-MM-DD)
    private val _selectedDate = MutableStateFlow(getCurrentDateString())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    // State for showing the "Add Event" dialog
    var showAddEventDialog by mutableStateOf(false)
        private set

    // All events flow
    val allEvents: StateFlow<List<Event>> = repository.allEvents.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Events filtered for the currently selected date
    val selectedDateEvents: StateFlow<List<Event>> = combine(allEvents, _selectedDate) { events, selectedDate ->
        events.filter { it.date == selectedDate }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun selectDate(dateString: String) {
        _selectedDate.value = dateString
    }

    fun nextMonth() {
        val next = _calendarState.value.clone() as Calendar
        next.add(Calendar.MONTH, 1)
        _calendarState.value = next
    }

    fun prevMonth() {
        val prev = _calendarState.value.clone() as Calendar
        prev.add(Calendar.MONTH, -1)
        _calendarState.value = prev
    }

    fun setYearMonth(year: Int, month: Int) {
        val update = _calendarState.value.clone() as Calendar
        update.set(Calendar.YEAR, year)
        update.set(Calendar.MONTH, month)
        _calendarState.value = update
    }

    fun openAddEventDialog() {
        showAddEventDialog = true
    }

    fun closeAddEventDialog() {
        showAddEventDialog = false
    }

    fun addEvent(title: String, description: String, time: String, colorHex: String) {
        viewModelScope.launch {
            val event = Event(
                title = title.ifBlank { "Untitled Event" },
                description = description,
                date = _selectedDate.value,
                time = time.ifBlank { "12:00" },
                colorHex = colorHex
            )
            repository.insert(event)
            closeAddEventDialog()
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            repository.delete(event)
        }
    }

    private fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }

    // Factory to easily construct AndroidViewModel
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EventViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

package com.example.data

import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {
    val allEvents: Flow<List<Event>> = eventDao.getAllEvents()

    fun getEventsForDate(date: String): Flow<List<Event>> = eventDao.getEventsForDate(date)

    suspend fun insert(event: Event) {
        eventDao.insertEvent(event)
    }

    suspend fun delete(event: Event) {
        eventDao.deleteEvent(event)
    }
}

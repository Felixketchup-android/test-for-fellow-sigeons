package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val date: String, // YYYY-MM-DD
    val time: String, // HH:mm
    val colorHex: String, // Hex string representing the event color
    val timestamp: Long = System.currentTimeMillis()
)

package com.example.ui

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class CalendarDay(
    val dayNumber: Int,
    val dateString: String, // "yyyy-MM-dd"
    val isCurrentMonth: Boolean,
    val isToday: Boolean
)

object CalendarGridHelper {
    fun generateGridDays(calendar: Calendar): List<CalendarDay> {
        val days = mutableListOf<CalendarDay>()
        
        // Clone the calendar so we don't modify the state
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, 1)
        
        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 2 = Monday, ...
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        
        // Let's get previous month's trailing days
        val prevCal = cal.clone() as Calendar
        prevCal.add(Calendar.MONTH, -1)
        val daysInPrevMonth = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        
        val leadingBlanks = firstDayOfWeek - 1 // For Sunday-start
        for (i in leadingBlanks - 1 downTo 0) {
            val day = daysInPrevMonth - i
            prevCal.set(Calendar.DAY_OF_MONTH, day)
            days.add(
                CalendarDay(
                    dayNumber = day,
                    dateString = formatDate(prevCal),
                    isCurrentMonth = false,
                    isToday = isSameDay(prevCal, Calendar.getInstance())
                )
            )
        }
        
        // Current month days
        for (day in 1..daysInMonth) {
            cal.set(Calendar.DAY_OF_MONTH, day)
            days.add(
                CalendarDay(
                    dayNumber = day,
                    dateString = formatDate(cal),
                    isCurrentMonth = true,
                    isToday = isSameDay(cal, Calendar.getInstance())
                )
            )
        }
        
        // Next month's leading days to complete the 6-week grid (42 cells)
        val remainingCells = 42 - days.size
        val nextCal = cal.clone() as Calendar
        nextCal.add(Calendar.MONTH, 1)
        for (day in 1..remainingCells) {
            nextCal.set(Calendar.DAY_OF_MONTH, day)
            days.add(
                CalendarDay(
                    dayNumber = day,
                    dateString = formatDate(nextCal),
                    isCurrentMonth = false,
                    isToday = isSameDay(nextCal, Calendar.getInstance())
                )
            )
        }
        
        return days
    }
    
    private fun formatDate(calendar: Calendar): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(calendar.time)
    }
    
    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}

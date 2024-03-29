package com.example.notesapp

import java.text.SimpleDateFormat
import java.util.*

data class  CalendarDateModel(var data: Date, var isSelected: Boolean = true) {

    val calendarDay: String
        get() = SimpleDateFormat("EE", Locale.getDefault()).format(data)

    val calendarYear: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(data)

    val calendarDate: String
        get() {
            val cal = Calendar.getInstance()
            cal.time = data
            return cal[Calendar.DAY_OF_MONTH].toString()
        }
}
package com.ranjit.galleryapplication.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToDateTime(millis: Long): String {
        // Convert milliseconds to LocalDateTime
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())

        // Define the desired date-time format
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm a")

        // Format the LocalDateTime using the formatter
        return localDateTime.format(formatter)
    }

    fun convertMillisToDateTimeBelowAPI26(millis: Long): String {
        // Create a Date object from milliseconds
        val date = Date(millis)

        // Create a Calendar object and set it to the specified date
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Define the desired date-time format
        val sdf = SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault())

        // Format the date using the SimpleDateFormat
        return sdf.format(calendar.time)
    }
}
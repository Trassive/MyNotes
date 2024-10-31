package com.example.mynotes.data.local
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Convertor {
    @TypeConverter
    fun fromTimestamp(value: String): LocalDate = value.let { LocalDate.parse(it, formatter) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate): String = date.format(formatter)
    companion object{
        private val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
    }
}
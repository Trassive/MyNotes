package com.example.mynotes.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int =0 ,
    val title: String,
    val content: String,
    val dateCreated: LocalDate,
    val lastModified: LocalDate
)
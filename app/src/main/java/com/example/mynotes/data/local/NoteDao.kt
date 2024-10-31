package com.example.mynotes.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes ORDER BY lastModified DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("UPDATE notes SET title = :title, content = :content, lastModified = :lastModified WHERE id = :id")
    suspend fun updateNote(id: Int, title: String, content: String, lastModified: LocalDate)

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): Note
}
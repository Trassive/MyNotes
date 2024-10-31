package com.example.mynotes.data.repository

import android.util.Log
import com.example.mynotes.data.local.Note
import com.example.mynotes.data.local.NoteDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class NotesRepository @Inject constructor(private val noteDao: NoteDao) {
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note): Long {
        return noteDao.insert(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
    suspend fun getNote(id: Int): Note{
        Log.d("NotesRepository", "Note id: $id}")
        return noteDao.getNoteById(id)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.id, note.title, note.content, note.lastModified)
    }
}
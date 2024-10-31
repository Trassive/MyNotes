package com.example.mynotes.ui.noteeditor

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.local.Note
import com.example.mynotes.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class NoteViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private var noteId = savedStateHandle.get<Int>("noteId")
    private var note: Note? =null
    val title = MutableStateFlow("")
    val content = MutableStateFlow("")

    init{
        noteId?.let {
            viewModelScope.launch {
                note = notesRepository.getNote(noteId!!)

                title.value = note!!.title
                content.value = note!!.content
            }
        }
        updateNote()

    }

    private fun updateNote() {
        viewModelScope.launch {
            combine(title, content) { title, content ->
                Note(
                    id = noteId ?: 0,
                    title = title,
                    content = content,
                    lastModified = LocalDate.now(),
                    dateCreated = note?.dateCreated ?: LocalDate.now()
                )
            }.distinctUntilChanged().debounce(1000).collect {note->
                if (note.title.isNotBlank() || note.content.isNotBlank()) {
                    if (note.id == 0) {
                         noteId = notesRepository.insert(note).toInt()
                    } else {
                        notesRepository.updateNote(note)
                    }
                }
            }
        }

    }

}
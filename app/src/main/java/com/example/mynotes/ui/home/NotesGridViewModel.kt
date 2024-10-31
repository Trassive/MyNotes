package com.example.mynotes.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.local.Note
import com.example.mynotes.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesGridViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel(){
    val state = repository.getAllNotes().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    private val _selectedItems = MutableStateFlow(listOf<Note>())
    val selectedItems = _selectedItems.asStateFlow()

    private val _isSelectionModeActive = MutableStateFlow(false)
    val isSelectionModeActive = _isSelectionModeActive.asStateFlow()

    private val selectedSet = mutableSetOf<Note>()

    fun toggleSelectedState(){
        _isSelectionModeActive.update {
            selectedSet.clear()
            !_isSelectionModeActive.value
        }
    }
    fun clearSelection() {
        _selectedItems.value = emptyList()
    }
    fun deleteSelectedItems(){
        viewModelScope.launch {
            selectedSet.forEach {
                repository.delete(it)
            }
        }
    }
    fun onItemClick(note: Note){
        if(selectedSet.contains(note)){
            selectedSet.remove(note)
            if(selectedSet.isEmpty()){
                toggleSelectedState()
            }
        } else {
            selectedSet.add(note)

        }
        _selectedItems.value = selectedSet.toList()
    }
    fun isSelected(note: Note) = selectedSet.contains(note)

}

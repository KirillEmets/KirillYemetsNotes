package com.kirillemets.kirillyemetsnotes.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kirillemets.kirillyemetsnotes.database.Note
import com.kirillemets.kirillyemetsnotes.database.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel(database: NoteDatabase) : ViewModel() {
    private val notesDao = database.notesDao()
    val allNotes = notesDao.getAll()

    fun onNoteClick() {

    }

    fun onNoteSwiped(note: Note) {
        deleteNote(note)
    }

    private fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesDao.delete(note)
        }
    }

    fun restoreNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesDao.insert(note)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class HomeScreenViewModelFactory(private val database: NoteDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            return HomeScreenViewModel(database = database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
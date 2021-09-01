package com.kirillemets.kirillyemetsnotes.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.kirillyemetsnotes.model.Note
import com.kirillemets.kirillyemetsnotes.model.network.remotedb.NoteRepository
import kotlinx.coroutines.flow.transform

class HomeScreenViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    private val allNotes = noteRepository.getAsFlow()
    val shownNotes = allNotes.transform { list -> emit(list.sortedByDescending { it.dateTime }) }

    fun onNoteClick() {

    }

    fun onNoteSwiped(note: Note) {
        deleteNote(note)
    }

    private fun deleteNote(note: Note) {
        noteRepository.delete(note)
    }

    fun restoreNote(note: Note) {
        noteRepository.insert(note)
    }
}

@Suppress("UNCHECKED_CAST")
class HomeScreenViewModelFactory(private val noteRepository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            return HomeScreenViewModel(noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
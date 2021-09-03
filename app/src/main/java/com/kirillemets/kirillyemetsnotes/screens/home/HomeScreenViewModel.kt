package com.kirillemets.kirillyemetsnotes.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.kirillyemetsnotes.model.Note
import com.kirillemets.kirillyemetsnotes.model.network.remotedb.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform

class HomeScreenViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    private val allNotes = noteRepository.getAsFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String>
        get() = _searchQuery

    val shownNotes = allNotes.combine(searchQuery) { list, query ->
        (if (query.isNotBlank()) {
            list.filter { it.text.contains(query) }
        } else
            list)
            .sortedByDescending { it.dateTime }.sortedByDescending { it.favorite }
    }


    private val _isSearching = mutableStateOf(false)
    val isSearching: State<Boolean>
        get() = _isSearching

    fun onNoteClick() {

    }

    fun onNoteSwiped(note: Note) {
        if (!note.favorite)
            deleteNote(note)
    }

    private fun deleteNote(note: Note) {
        noteRepository.delete(note)
    }

    fun restoreNote(note: Note) {
        noteRepository.insert(note)
    }

    fun changeSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearchQuery() {
        changeSearchQuery("")
    }

    fun startSearch() {
        _isSearching.value = true
    }

    fun stopSearch() {
        clearSearchQuery()
        _isSearching.value = false
    }
}

@Suppress("UNCHECKED_CAST")
class HomeScreenViewModelFactory(private val noteRepository: NoteRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            return HomeScreenViewModel(noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
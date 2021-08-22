package com.kirillemets.kirillyemetsnotes.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kirillemets.kirillyemetsnotes.database.NoteDatabase
import com.kirillemets.kirillyemetsnotes.database.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditScreenViewModel(noteId: Long, database: NoteDatabase): ViewModel() {
    private val notesDao = database.notesDao()
    private val _text: MutableState<String> = mutableStateOf("")
    val text: State<String> = _text
    private lateinit var note: Note

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                note = notesDao.get(noteId)
            }
            _text.value = note.text
        }
    }

    fun onTextChange(newText: String) {
        _text.value = newText
    }

    fun saveChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = note.copy(text = _text.value)
            notesDao.update(newNote)
        }
    }
}

class EditScreenViewModelFactory(private val noteId: Long, val database: NoteDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EditScreenViewModel::class.java)) {
            return EditScreenViewModel(noteId = noteId, database = database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
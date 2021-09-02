package com.kirillemets.kirillyemetsnotes.screens.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kirillemets.kirillyemetsnotes.model.Note
import com.kirillemets.kirillyemetsnotes.model.network.remotedb.NoteRepository
import kotlinx.coroutines.*
import org.joda.time.DateTime
import java.util.*

class EditScreenViewModel(noteId: String, private val noteRepository: NoteRepository) :
    ViewModel() {
    private val _text: MutableState<String> = mutableStateOf("")
    val text: State<String> = _text

    private val _isFavorite: MutableState<Boolean> = mutableStateOf(false)
    val isFavorite: State<Boolean> = _isFavorite

    var note: Note? = null

    init {
        if (noteId != "new")
            viewModelScope.launch {
                note = noteRepository.get(noteId)
                _text.value = note?.text ?: ""
                _isFavorite.value = note?.favorite ?: false
            }
    }

    fun onTextChange(newText: String) {
        _text.value = newText
    }

    fun changeFavorite() {
        _isFavorite.value = !_isFavorite.value
    }

    private fun saveChanges() {
        val millis = DateTime().millis

        if (note == null) {
            val newId = UUID.randomUUID().toString()
            val newNote = Note(
                noteId = newId,
                text = text.value,
                dateTime = millis,
                favorite = isFavorite.value
            )
            noteRepository.insert(newNote)
            return
        }

        val newNote = note!!.copy(text = text.value, dateTime = millis, favorite = isFavorite.value)
        noteRepository.update(newNote)

    }

    fun onNavigateUp(saveChanges: Boolean) {
        if (saveChanges) {
            if (text.value.isBlank()) {
                deleteCurrentNote()
                return
            }
            saveChanges()
            return
        }
    }

    private fun deleteCurrentNote() {
        note?.let { noteRepository.delete(it) }
    }
}

@Suppress("UNCHECKED_CAST")
class EditScreenViewModelFactory(
    private val noteId: String,
    private val noteRepository: NoteRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditScreenViewModel::class.java)) {
            return EditScreenViewModel(noteId = noteId, noteRepository = noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
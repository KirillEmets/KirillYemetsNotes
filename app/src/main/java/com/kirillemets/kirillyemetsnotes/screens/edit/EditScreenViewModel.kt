package com.kirillemets.kirillyemetsnotes.screens.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kirillemets.kirillyemetsnotes.model.database.Note
import com.kirillemets.kirillyemetsnotes.model.network.remotedb.NoteRepository
import kotlinx.coroutines.*
import org.joda.time.DateTime
import kotlin.random.Random
import kotlin.random.nextUInt

class EditScreenViewModel(noteId: String, private val noteRepository: NoteRepository) :
    ViewModel() {
    private val _text: MutableState<String> = mutableStateOf("")
    val text: State<String> = _text
    var note: Note? = null

    init {
        if (noteId != "new")
            viewModelScope.launch {
                note = noteRepository.get(noteId)
                _text.value = note?.text ?: ""
            }
    }

    fun onTextChange(newText: String) {
        _text.value = newText
    }

    private fun saveChanges() {
        val millis = DateTime().millis

        if (note == null) {
            val newId = "id_${Random.nextUInt()}"
            val newNote = Note(
                noteId = newId,
                text = _text.value,
                dateTime = millis
            )
            noteRepository.insert(newNote)
            return
        }

        val newNote = note!!.copy(text = _text.value, dateTime = millis)
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
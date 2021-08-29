package com.kirillemets.kirillyemetsnotes.screens.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kirillemets.kirillyemetsnotes.database.NoteDatabase
import com.kirillemets.kirillyemetsnotes.database.Note
import kotlinx.coroutines.*
import org.joda.time.DateTime
import kotlin.random.Random
import kotlin.random.nextUInt

class EditScreenViewModel(noteId: String, database: NoteDatabase) : ViewModel() {
    private val notesDao = database.notesDao()
    private val _text: MutableState<String> = mutableStateOf("")
    val text: State<String> = _text
    var note: Note? = null

    init {
        if (noteId != "new")
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    note = notesDao.get(noteId)
                }
                _text.value = note?.text ?: ""
            }
    }

    fun onTextChange(newText: String) {
        _text.value = newText
    }

    private fun saveChanges() {
        val millis = DateTime().millis

        runBlocking(Dispatchers.IO) {
            if (note == null) {
                val newId = "id_${Random.nextUInt()}"
                val newNote = Note(
                    noteId = newId,
                    text = _text.value,
                    dateTime = millis
                )
                notesDao.insert(newNote)
                return@runBlocking
            }

            val newNote = note!!.copy(text = _text.value, dateTime = millis)
            notesDao.update(newNote)
        }
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
        runBlocking (Dispatchers.IO) {
            note?.let { notesDao.delete(it) }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class EditScreenViewModelFactory(private val noteId: String, private val database: NoteDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditScreenViewModel::class.java)) {
            return EditScreenViewModel(noteId = noteId, database = database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
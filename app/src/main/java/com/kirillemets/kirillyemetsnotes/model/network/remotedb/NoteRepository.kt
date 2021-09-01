package com.kirillemets.kirillyemetsnotes.model.network.remotedb

import com.kirillemets.kirillyemetsnotes.model.Note
import kotlinx.coroutines.*

class NoteRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun getAsFlow() = FirestoreNotesService.getAllAsFlow()

    fun insert(note: Note) {
        coroutineScope.launch {
            FirestoreNotesService.insert(note)
        }
    }

    fun update(note: Note) {
        coroutineScope.launch {
            FirestoreNotesService.update(note)
        }
    }

    fun delete(note: Note) {
        coroutineScope.launch {
            FirestoreNotesService.delete(note)
        }
    }

    suspend fun get(noteId: String): Note {
        return withContext(coroutineScope.coroutineContext) {
            FirestoreNotesService.get(noteId)
        }
    }
}
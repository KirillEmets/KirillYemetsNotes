package com.kirillemets.kirillyemetsnotes.model.network.remotedb

import android.content.Context
import com.kirillemets.kirillyemetsnotes.model.database.Note
import com.kirillemets.kirillyemetsnotes.model.database.NoteDatabase
import kotlinx.coroutines.*

class NoteRepository(context: Context) {
//    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val room = NoteDatabase.getInstance(context).notesDao()
    suspend fun getAll(): List<Note> {
        return withContext(coroutineScope.coroutineContext) {
            room.getAllSuspend()
        }
    }

    fun getAsFlow() = room.getAll()

    fun insert(note: Note) {
        coroutineScope.launch {
            room.insert(note)
        }
    }

    fun update(note: Note) {
        coroutineScope.launch {
            room.update(note)
        }
    }

    fun delete(note: Note) {
        coroutineScope.launch {
            room.delete(note)
        }
    }

    suspend fun get(noteId: String): Note {
        return withContext(coroutineScope.coroutineContext) {
            room.get(noteId)
        }
    }
}
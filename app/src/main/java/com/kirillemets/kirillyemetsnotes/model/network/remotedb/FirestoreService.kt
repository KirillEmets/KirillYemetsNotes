package com.kirillemets.kirillyemetsnotes.model.network.remotedb

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.kirillemets.kirillyemetsnotes.model.Note
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object FirestoreNotesService {
    private val db: FirebaseFirestore
        get() {
            val db = FirebaseFirestore.getInstance()
            db.firestoreSettings = firestoreSettings {
                isPersistenceEnabled = true
            }
            return db
        }

    fun getAllAsFlow(): Flow<List<Note>> = callbackFlow {
        val subscription = collectionRef?.addSnapshotListener {snapshot, _ ->
            snapshot?.toObjects(Note::class.java)?.let {
                trySend(it)
            }
        }
        awaitClose { subscription?.remove() }
    }


    fun update(note: Note) {
        noteDocRef(note.noteId)?.set(note)
    }

    fun insert(note: Note) {
        noteDocRef(note.noteId)?.set(note)
    }

    fun delete(note: Note) {
        noteDocRef(note.noteId)?.delete()
    }

    private fun noteDocRef(noteId: String): DocumentReference? =
        FirebaseAuth.getInstance().currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("notes")
                .document(noteId)
        }

    private val collectionRef: CollectionReference?
        get() = FirebaseAuth.getInstance().currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("notes")
        }


    private suspend fun <T> Task<T>.await(): T {
        val result = CompletableDeferred<T>()
        addOnSuccessListener {
            result.complete(it)
        }
        addOnFailureListener {
            result.completeExceptionally(it)
        }
        return result.await()
    }

    suspend fun get(noteId: String): Note {
        return noteDocRef(noteId)?.get()?.await()?.toObject(Note::class.java) ?: throw Exception("No such note")
    }
}


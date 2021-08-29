package com.kirillemets.kirillyemetsnotes.model.network.remotedb

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred

object FirestoreNotesService {
    fun getAll() {

    }
}

suspend fun<T> Task<T>.await(): T {
    val result = CompletableDeferred<T>()
    addOnSuccessListener {
        result.complete(it)
    }
    addOnFailureListener {
        result.completeExceptionally(it)
    }
    val a = result.await()
    return a
}
package com.kirillemets.kirillyemetsnotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity
data class Note(
    @DocumentId
    @PrimaryKey
    val noteId: String = "",
    val text: String = "",
    val dateTime: Long = 0,
    val favorite: Boolean = false,
)
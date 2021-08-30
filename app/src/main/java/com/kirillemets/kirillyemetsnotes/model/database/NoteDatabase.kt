package com.kirillemets.kirillyemetsnotes.model.database

import android.content.Context
import androidx.room.*
import com.google.firebase.firestore.DocumentId

@Entity
data class Note(
    @DocumentId
    @PrimaryKey
    val noteId: String,
    val text: String = "",
    val dateTime: Long = 0,
    val favorite: Boolean = false,
)

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    companion object {
        private var Instance: NoteDatabase? = null
        fun getInstance(context: Context): NoteDatabase {
            if (Instance == null) {
                Instance = Room.databaseBuilder(
                    context,
                    NoteDatabase::class.java,
                    "notes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return Instance ?: throw Exception("Database is broken")
        }
    }

    abstract fun notesDao(): NoteDao
}
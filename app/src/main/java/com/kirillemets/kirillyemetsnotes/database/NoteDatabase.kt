package com.kirillemets.kirillyemetsnotes.database

import android.content.Context
import androidx.room.*

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val noteId: Long = 0,
    val text: String = "",
    val date: Long = 0, // TODO add time
    val favorite: Boolean = false,
)

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {
    companion object {
        private var Instance: NoteDatabase? = null
        fun getInstance(context: Context): NoteDatabase {
            synchronized(this) {
                var instance = Instance

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        NoteDatabase::class.java,
                        "notes_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }
    abstract fun notesDao(): NoteDao
}
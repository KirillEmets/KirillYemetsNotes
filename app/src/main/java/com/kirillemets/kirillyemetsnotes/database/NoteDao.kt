package com.kirillemets.kirillyemetsnotes.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note ORDER BY noteId DESC")
    fun getAll(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE noteId = :noteId")
    suspend fun get(noteId: Long): Note

    @Insert
    suspend fun insert(card: Note): Long

    @Update
    suspend fun update(card: Note)

    @Delete
    suspend fun delete(card: Note)
}

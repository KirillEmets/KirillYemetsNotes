package com.kirillemets.kirillyemetsnotes.model.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note ORDER BY dateTime DESC")
    fun getAll(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY dateTime DESC")
    suspend fun getAllSuspend(): List<Note>

    @Query("SELECT * FROM note WHERE noteId = :noteId")
    suspend fun get(noteId: String): Note

    @Query("DELETE FROM note WHERE noteId = :noteId")
    fun deleteById(noteId: String)

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}

package com.ei1039_1048.notesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAll(): Flow<List<LocalNote>>

    @Insert
    suspend fun insert(note: LocalNote)

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE note SET title = :title, description = :description WHERE id = :id")
    suspend fun update(id: String, title: String, description: String)
}
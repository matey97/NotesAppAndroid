package com.ei1039_1048.notesapp.data

import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotesStream(): Flow<List<Note>>

    suspend fun insert(note: Note)

    suspend fun update(id: String, title: String, description: String)

    suspend fun delete(id: String)
}
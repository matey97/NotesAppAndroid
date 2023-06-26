package com.ei1039_1048.notesapp.data

import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotesStream(): Flow<List<Note>>

    suspend fun insert(note: Note)
}
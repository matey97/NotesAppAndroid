package com.ei1039_1048.notesapp

import com.ei1039_1048.notesapp.data.Note
import com.ei1039_1048.notesapp.data.NoteRepository
import kotlinx.coroutines.flow.Flow

class NotesController(
    private val notesRepository: NoteRepository
) {

    fun getNotesStream(): Flow<List<Note>> {
        throw NotImplementedError("TODO")
    }

    fun createNote(title: String, description: String) {
        throw NotImplementedError("TODO")
    }
}
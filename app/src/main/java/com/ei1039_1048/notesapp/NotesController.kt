package com.ei1039_1048.notesapp

import com.ei1039_1048.notesapp.data.Note
import com.ei1039_1048.notesapp.data.NoteRepository
import com.ei1039_1048.notesapp.exceptions.EmptyTitleException
import com.ei1039_1048.notesapp.exceptions.NoteNotFoundException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NotesController(
    private val notesRepository: NoteRepository
) {

    fun getNotesStream(): Flow<List<Note>> = notesRepository.getNotesStream()

    suspend fun createNote(title: String, description: String) {
        if (title.isEmpty()) {
            throw EmptyTitleException()
        }

        val note = Note(title = title, description = description)
        notesRepository.insert(note)
    }

    suspend fun updateNote(id: String, title: String, description: String) {
        if (title.isEmpty()) {
            throw EmptyTitleException()
        }

        if (!idExists(id)) {
            throw NoteNotFoundException(id)
        }

        notesRepository.update(id, title, description)
    }

    suspend fun deleteNote(id: String) {
        throw NotImplementedError("TODO")
    }

    private suspend fun idExists(id: String): Boolean = getNotesStream().first().any { it.id == id }
}
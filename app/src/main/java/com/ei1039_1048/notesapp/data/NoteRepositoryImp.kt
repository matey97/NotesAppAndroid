package com.ei1039_1048.notesapp.data

import com.ei1039_1048.notesapp.data.local.LocalNote
import com.ei1039_1048.notesapp.data.local.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImp(
    private val localStorage: NoteDao
) : NoteRepository {

    override fun getNotesStream(): Flow<List<Note>> {
        return localStorage.getAll()
            .map { notes -> notes.map(LocalNote::toUI) }
    }

    override suspend fun insert(note: Note) {
        localStorage.insert(note.toLocal())
    }
}
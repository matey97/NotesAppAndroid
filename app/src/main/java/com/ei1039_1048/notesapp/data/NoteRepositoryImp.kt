package com.ei1039_1048.notesapp.data

import kotlinx.coroutines.flow.Flow

class NoteRepositoryImp : NoteRepository {

    override fun getNotesStream(): Flow<List<Note>> {
        throw NotImplementedError("TODO")
    }

    override suspend fun insert(note: Note) {
        throw NotImplementedError("TODO")
    }
}
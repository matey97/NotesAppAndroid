package com.ei1039_1048.notesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalNote::class], version = 1)
abstract class NotesAppDatabase : RoomDatabase() {
    abstract fun noteDAO(): NoteDao
}
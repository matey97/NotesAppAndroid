package com.ei1039_1048.notesapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ei1039_1048.notesapp.data.NoteRepository
import com.ei1039_1048.notesapp.data.NoteRepositoryImp
import com.ei1039_1048.notesapp.data.local.NotesAppDatabase
import com.ei1039_1048.notesapp.exceptions.EmptyTitleException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NotesControllerAcceptanceTests {

    private lateinit var database: NotesAppDatabase
    private lateinit var noteRepository: NoteRepository
    private lateinit var notesController: NotesController

    private val emptyTitle = ""
    private val title1 = "Test title 1"
    private val description1 = "Test description 1"
    private val title2 = "Test title 2"
    private val description2 = "Test description 2"

    @Before
    fun setup() {
        // Usamos una DB distinta a la real. En este caso, se usa una DB en memoria.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesAppDatabase::class.java
        ).allowMainThreadQueries().build()

        noteRepository = NoteRepositoryImp(database.noteDAO())
        notesController = NotesController(noteRepository)
    }

    @Test
    fun HU01_E01() = runBlocking {
        // Given: no hay ninguna nota

        // When: se intenta crear una nota con título y descripción
        notesController.createNote(title1, description1)

        // Then: se almacena la nota con el titulo y descripción dados
        val notes = notesController.getNotesStream().first()
        assertEquals(1, notes.size)
        assertEquals(title1, notes[0].title)
        assertEquals(description1, notes[0].description)
    }

    @Test(expected = EmptyTitleException::class)
    fun HU01_E02() = runBlocking {
        // Given: no hay ninguna nota

        // When: se intenta crear una nota sin título
        notesController.createNote(emptyTitle, description1)

        // Then: se lanza la excepción EmptyTitleException
    }

    @Test
    fun HU02_E01() = runBlocking {
        // Given: no hay ninguna nota

        // When: se consultan las notas
        val notes = notesController.getNotesStream().first()

        // Then: se obtiene una lista vacía
        assertEquals(0, notes.size)
    }

    @Test
    fun HU02_E02() = runBlocking {
        // Given: hay varias notas almacenadas
        notesController.createNote(title1, description1)
        notesController.createNote(title2, description2)

        // When: se consultan las notas
        val notes = notesController.getNotesStream().first()

        // Then: se obtiene una lista con las notas almacenadas
        assertEquals(2, notes.size)
        assertEquals(title1, notes[0].title)
        assertEquals(description1, notes[0].description)
        assertEquals(title2, notes[1].title)
        assertEquals(description2, notes[1].description)
    }
}
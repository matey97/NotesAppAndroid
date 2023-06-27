package com.ei1039_1048.notesapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ei1039_1048.notesapp.data.NoteRepository
import com.ei1039_1048.notesapp.data.NoteRepositoryImp
import com.ei1039_1048.notesapp.data.local.NotesAppDatabase
import com.ei1039_1048.notesapp.exceptions.EmptyTitleException
import com.ei1039_1048.notesapp.exceptions.NoteNotFoundException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After

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

    @Test
    fun HU03_E01() = runBlocking {
        // Given: hay varias notas almacenadas
        notesController.createNote(title1, description1)
        notesController.createNote(title2, description2)

        val notesListFlow = notesController.getNotesStream()
        var notes = notesListFlow.take(1).first()
        val noteId2 = notes[1].id

        // When: se intenta cambiar el contenido de una nota
        val newTitle = "New note title"
        notesController.updateNote(noteId2, newTitle, description2)

        // Then: la nota se actualiza correctamente
        notes = notesListFlow.take(1).first()
        assertEquals(2, notes.size)
        assertEquals(newTitle, notes[1].title)
        assertEquals(description2, notes[1].description)
    }

    @Test(expected = EmptyTitleException::class)
    fun HU03_E02() = runBlocking {
        // Given: hay varias notas almacenadas
        notesController.createNote(title1, description1)
        notesController.createNote(title2, description2)

        val notes = notesController.getNotesStream().first()
        val noteId2 = notes[1].id

        // When: se intenta cambiar el contenido de una nota con un título inválido
        val newTitle = ""
        notesController.updateNote(noteId2, newTitle, description2)

        // Then: se lanza la excepción EmptyTitleException
    }

    @Test(expected = NoteNotFoundException::class)
    fun HU03_E03() = runBlocking {
        // Given: hay varias notas almacenadas
        notesController.createNote(title1, description1)
        notesController.createNote(title2, description2)

        // When: se intenta cambiar el contenido de una nota usando un id inválido
        val newTitle = "Other title"
        notesController.updateNote("", newTitle, description2)

        // Then: se lanza la excepción NoteNotFoundException
    }

    @Test
    fun HU04_E01() = runBlocking {
        // Given: hay varias notas almacenadas
        notesController.createNote(title1, description1)
        notesController.createNote(title2, description2)

        val notesListFlow = notesController.getNotesStream()
        var notes = notesListFlow.take(1).first()
        val noteId1 = notes[0].id

        // When: se intenta borrar una nota usando un id inválido
        notesController.deleteNote(noteId1)

        // Then: se elimina la nota de la base de datos
        notes = notesListFlow.take(1).first()
        assertEquals(1, notes.size)
        assertEquals(title2, notes[0].title)
        assertEquals(description2, notes[0].description)

    }

    @Test(expected = NoteNotFoundException::class)
    fun HU04_E02() = runBlocking {
        // Given: hay varias notas almacenadas
        notesController.createNote(title1, description1)
        notesController.createNote(title2, description2)

        // When: se intenta borrar una nota usando un id inválido
        notesController.deleteNote("")

        // Then: se lanza la excepción NoteNotFoundException
    }

    @After
    fun clean() {
        // Limpiamos y cerramos la DB, aunque no sería necesario
        database.clearAllTables()
        database.close()
    }
}
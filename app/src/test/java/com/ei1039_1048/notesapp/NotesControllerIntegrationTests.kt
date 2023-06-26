package com.ei1039_1048.notesapp

import com.ei1039_1048.notesapp.data.Note
import com.ei1039_1048.notesapp.data.NoteRepository
import com.ei1039_1048.notesapp.exceptions.EmptyTitleException
import com.ei1039_1048.notesapp.exceptions.NoteNotFoundException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class NotesControllerIntegrationTests {

    @Mock
    private lateinit var noteRepositoryMock: NoteRepository
    private lateinit var notesController: NotesController

    private val emptyTitle = ""
    private val id1 = "id1"
    private val title1 = "Test title 1"
    private val description1 = "Test description 1"
    private val id2 = "id2"
    private val title2 = "Test title 2"
    private val description2 = "Test description 2"

    private val note1 = Note(
        id = id1,
        title = title1,
        description = description1
    )

    private val note2 = Note(
        id = id2,
        title = title2,
        description = description2
    )

    @Before
    fun setup() {
        // Inyectamos al controlador el mock del repositorio
        notesController = NotesController(noteRepositoryMock)
    }

    @Test
    fun HU01_E01() = runBlocking {
        // Given: no hay ninguna nota
        whenever(noteRepositoryMock.getNotesStream()).thenReturn(flow {
            emit(emptyList())
        })

        // When: se intenta crear una nota con título y descripción
        notesController.createNote(title1, description1)

        // Then: se almacena la nota con el titulo y descripción dados (se llama a mock.insert con una nota)
        verify(noteRepositoryMock).insert(any<Note>())
    }

    @Test(expected = EmptyTitleException::class)
    fun HU01_E02() = runBlocking {
        // Given: no hay ninguna nota
        whenever(noteRepositoryMock.getNotesStream()).thenReturn(flow {
            emit(emptyList())
        })

        // When: se intenta crear una nota sin título
        notesController.createNote(emptyTitle, description1)

        // Then: se lanza la excepción EmptyTitleException
    }

    @Test
    fun HU02_E01() = runBlocking {
        // Given: no hay ninguna nota
        whenever(noteRepositoryMock.getNotesStream()).thenReturn(flow {
            emit(emptyList())
        })

        // When: se consultan las notas
        val notes = notesController.getNotesStream().first()

        // Then: se obtiene una lista vacía (del mock)
        verify(noteRepositoryMock).getNotesStream()
        assertEquals(0, notes.size)
    }

    @Test
    fun HU02_E02() = runBlocking {
        // Given: hay varias notas almacenadas
        whenever(noteRepositoryMock.getNotesStream()).thenReturn(flow {
            emit(listOf(note1, note2))
        })

        // When: se consultan las notas
        val notes = notesController.getNotesStream().first()

        // Then: se obtiene una lista con las notas almacenadas (del mock)
        verify(noteRepositoryMock).getNotesStream()
        assertEquals(2, notes.size)
        assertEquals(title1, notes[0].title)
        assertEquals(description1, notes[0].description)
        assertEquals(title2, notes[1].title)
        assertEquals(description2, notes[1].description)
    }

    @Test
    fun HU03_E01() = runBlocking {
        // Given: hay varias notas almacenadas
        whenever(noteRepositoryMock.getNotesStream()).thenReturn(flow {
            emit(listOf(note1, note2))
        })

        // When: se intenta cambiar el contenido de una nota
        val newTitle = "New note title"
        notesController.updateNote(note2.id, newTitle, description2)

        // Then: la nota se actualiza correctamente (se llama a mock.update con los argumentos correctos)
        verify(noteRepositoryMock).update(id2, newTitle, description2)
    }

    @Test(expected = EmptyTitleException::class)
    fun HU03_E02() = runBlocking {
        // Given: hay varias notas almacenadas
        whenever(noteRepositoryMock.getNotesStream()).thenReturn(flow {
            emit(listOf(note1, note2))
        })

        // When: se intenta cambiar el contenido de una nota con un título inválido
        val newTitle = ""
        notesController.updateNote(id2, newTitle, description2)

        // Then: se lanza la excepción EmptyTitleException
    }

    @Test(expected = NoteNotFoundException::class)
    fun HU03_E03() = runBlocking {
        // Given: hay varias notas almacenadas
        whenever(noteRepositoryMock.getNotesStream()).thenReturn(flow {
            emit(listOf(note1, note2))
        })

        // When: se intenta cambiar el contenido de una nota usando un id inválido
        val newTitle = "Other title"
        notesController.updateNote("", newTitle, description2)

        // Then: se lanza la excepción NoteNotFoundException
    }

    @Test
    fun HU04_E01() = runBlocking {
        // Given: hay varias notas almacenadas
        whenever(noteRepositoryMock.getNotesStream()).thenReturn(flow {
            emit(listOf(note1, note2))
        })

        // When: se intenta borrar una nota usando un id inválido
        notesController.deleteNote(id1)

        // Then: se elimina la nota de la base de datos (se llama a mock.delete con los argumentos correctos)
        verify(noteRepositoryMock).delete(id1)
    }

    @Test(expected = NoteNotFoundException::class)
    fun HU04_E02() = runBlocking {
        // Given: hay varias notas almacenadas
        whenever(noteRepositoryMock.getNotesStream()).thenReturn(flow {
            emit(listOf(note1, note2))
        })

        // When: se intenta borrar una nota usando un id inválido
        notesController.deleteNote("")

        // Then: se lanza la excepción NoteNotFoundException
    }

    @After
    fun clean() {
        // Reseteamos la configuración y estado del mock
        reset(noteRepositoryMock)
    }
}
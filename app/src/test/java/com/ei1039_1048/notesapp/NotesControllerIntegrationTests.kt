package com.ei1039_1048.notesapp

import com.ei1039_1048.notesapp.data.Note
import com.ei1039_1048.notesapp.data.NoteRepository
import com.ei1039_1048.notesapp.exceptions.EmptyTitleException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
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
}
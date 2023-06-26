package com.ei1039_1048.notesapp

import com.ei1039_1048.notesapp.data.Note
import com.ei1039_1048.notesapp.data.NoteRepository
import com.ei1039_1048.notesapp.exceptions.EmptyTitleException
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
    private val title1 = "Test title 1"
    private val description1 = "Test description 1"

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
}
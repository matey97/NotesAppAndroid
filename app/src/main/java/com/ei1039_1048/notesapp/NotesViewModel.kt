package com.ei1039_1048.notesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ei1039_1048.notesapp.data.Note
import com.ei1039_1048.notesapp.exceptions.EmptyTitleException
import com.ei1039_1048.notesapp.exceptions.NoteNotFoundException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/*
Representa el estado de la interfaz. Definimos el estado como:

- notes: lista de notas
- isLoading: indica si la lista de notas se esta cargando. Si true, se mostrará un indicador de carga
- snackBarMessage: mensaje que debe mostrarse en un snackbar. Si es una cadena válida, se mostrará el snackbar

IMPORTANTE: el ViewModel no llama a ningún método para mostrar el indicador de carga o el snackbar,
simplemente contiene el ESTADO de la interfaz. Es la interfaz la que reacciona a los cambios del
ESTADO y muestra el indicador o el snackbar.
*/
data class NotesUIState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val snackBarMessage: String? = null
)

class NotesViewModel(
    private val notesController: NotesController
) : ViewModel() {

    private var _notesList = notesController.getNotesStream()
    private var _isLoading = MutableStateFlow(false)
    private var _snackBarMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<NotesUIState> = combine(
        _notesList,
        _isLoading,
        _snackBarMessage
    ) {
            notesList, isLoading, snackBarMessage ->
        NotesUIState(notesList, isLoading, snackBarMessage)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotesUIState(isLoading = true)
    )

    fun createNote(title: String, description: String) = viewModelScope.launch {
        try {
            notesController.createNote(title, description)
            _snackBarMessage.value = "¡Nota creada con exito!"
        } catch (ex: EmptyTitleException) {
            _snackBarMessage.value = "¡La nota debe tener un título!"
        }
    }

    fun updateNote(id: String, title: String, description: String) = viewModelScope.launch {
        try {
            notesController.updateNote(id, title, description)
            _snackBarMessage.value = "¡Nota actualizada con exito!"
        } catch (ex: EmptyTitleException) {
            _snackBarMessage.value = "¡La nota debe tener un título!"
        } catch (ex: NoteNotFoundException) {
            // Podríamos mostrar también un snackbar
        }
    }

    fun deleteNote(id: String) = viewModelScope.launch {
        try {
            notesController.deleteNote(id)
        } catch (ex: NoteNotFoundException) {
            // Podríamos mostrar también un snackbar
        }
    }

    fun hideSnackbar() =viewModelScope.launch {
        _snackBarMessage.value = null
    }
}
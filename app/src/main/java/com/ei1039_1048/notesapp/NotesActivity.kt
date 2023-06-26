package com.ei1039_1048.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import com.ei1039_1048.notesapp.data.Note
import com.ei1039_1048.notesapp.data.NoteRepositoryImp
import com.ei1039_1048.notesapp.data.local.NotesAppDatabase
import com.ei1039_1048.notesapp.ui.dialog.CreateNoteDialog
import com.ei1039_1048.notesapp.ui.dialog.UpdateNoteDialog
import com.ei1039_1048.notesapp.ui.note.EmptyNoteItemsList
import com.ei1039_1048.notesapp.ui.note.LoadingIndicator
import com.ei1039_1048.notesapp.ui.note.NoteItemsList
import com.ei1039_1048.notesapp.ui.theme.NotesAppTheme

const val APP_NAME = "notes-app-db"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            NotesAppDatabase::class.java,
            APP_NAME
        ).build()
        val noteDAO = db.noteDAO()
        val noteRepository = NoteRepositoryImp(noteDAO)
        val notesController = NotesController(noteRepository)

        setContent {
            val viewModel: NotesViewModel by viewModels { ViewModelFactory(notesController) }
            NotesApp(notesViewModel = viewModel)
        }
    }
}

@Composable
fun NotesApp(
    modifier: Modifier = Modifier,
    notesViewModel: NotesViewModel
) {
    NotesAppTheme(dynamicColor = false) {
        val uiState by notesViewModel.uiState.collectAsStateWithLifecycle()

        var showCreateNoteDialog by rememberSaveable { mutableStateOf(false) }
        var editNote by remember { mutableStateOf<Note?>(null) }

        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            modifier = modifier,
            topBar = { NotesTopBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = { NotesAddFAB { showCreateNoteDialog = true } }
        ) {
            if (uiState.isLoading) {
                LoadingIndicator(Modifier.padding(it))
            } else if (uiState.notes.isEmpty()) {
                EmptyNoteItemsList(Modifier.padding(it))
            } else {
                NoteItemsList(
                    modifier = Modifier.padding(it),
                    notes = uiState.notes,
                    onEditNoteTap = { note -> editNote = note },
                    onDeleteNoteTap = { note -> notesViewModel.deleteNote(note.id) }
                )
            }

            uiState.snackBarMessage?.let { message ->
                LaunchedEffect(snackbarHostState, notesViewModel, message) {
                    snackbarHostState.showSnackbar(
                        message,
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                    notesViewModel.hideSnackbar()
                }
            }

            if (showCreateNoteDialog) {
                CreateNoteDialog(
                    onDialogClosed = { showCreateNoteDialog = false },
                    onNoteCreated = { title, description -> notesViewModel.createNote(title, description)}
                )
            }

            if (editNote != null) {
                UpdateNoteDialog(
                    note = editNote!!,
                    onDialogClosed = { editNote = null },
                    onNoteUpdated = { id, title, description -> notesViewModel.updateNote(id, title, description)}
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopBar() {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(4.dp))
                Icon(imageVector = Icons.Default.Create, contentDescription = "")
            }
        },
    )
}

@Composable
fun NotesAddFAB(
    onAddNoteTap: () -> Unit
) {
    FloatingActionButton(
        onClick = onAddNoteTap,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
    }
}
package com.ei1039_1048.notesapp.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ei1039_1048.notesapp.R
import com.ei1039_1048.notesapp.data.Note
import com.ei1039_1048.notesapp.ui.theme.NotesAppTheme

/*
Aquí se sigue una práctica recomendada en Jetpack Compose:
Se crea una función "base" sin estado ("stateless") la cual se usa
posteriormente en otra función que contiene el estado ("statefull")

Su ventaja es que facilita la reutilización del componente "stateless"
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseDialog(
    dialogTitle: String,
    dialogDismissText: String,
    dialogCompleteText: String,
    noteTitle: String,
    noteDescription: String,
    onNoteTitleChanged: (String) -> Unit,
    onNoteDescriptionChanged: (String) -> Unit,
    onDialogCompleted: () -> Unit,
    onDialogClosed: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDialogClosed,
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogTitle(title = dialogTitle)
                Spacer(modifier = Modifier.size(8.dp))
                DialogInputBox(
                    value = noteTitle,
                    label = stringResource(R.string.title_box_label),
                    onValueChanged = onNoteTitleChanged
                )
                Spacer(modifier = Modifier.size(4.dp))
                DialogInputBox(
                    value = noteDescription,
                    label = stringResource(R.string.description_box_label),
                    onValueChanged = onNoteDescriptionChanged
                )
                Spacer(modifier = Modifier.size(8.dp))
                DialogActionButtons(
                    dismissText = dialogDismissText,
                    onDismissAction = onDialogClosed,
                    completeText = dialogCompleteText,
                    onCompleteAction = {
                        onDialogCompleted()
                        onDialogClosed()
                    }
                )
            }
        }
    }
}

@Composable
fun CreateNoteDialog(
    onDialogClosed: () -> Unit,
    onNoteCreated: (String, String) -> Unit
) {
    var noteTitle by rememberSaveable { mutableStateOf("") }
    var noteDescription by rememberSaveable { mutableStateOf("") }

    BaseDialog(
        dialogTitle = stringResource(R.string.create_dialog_title),
        dialogDismissText = stringResource(R.string.cancel_button_text),
        dialogCompleteText = stringResource(R.string.create_button_text),
        noteTitle = noteTitle,
        noteDescription = noteDescription,
        onNoteTitleChanged = { noteTitle = it },
        onNoteDescriptionChanged = { noteDescription = it},
        onDialogCompleted = { onNoteCreated(noteTitle, noteDescription) },
        onDialogClosed = onDialogClosed
    )
}

@Composable
fun UpdateNoteDialog(
    note: Note,
    onDialogClosed: () -> Unit,
    onNoteUpdated: (String, String, String) -> Unit
) {
    var noteTitle by rememberSaveable { mutableStateOf(note.title) }
    var noteDescription by rememberSaveable { mutableStateOf(note.description) }

    BaseDialog(
        dialogTitle = stringResource(R.string.update_dialog_title),
        dialogDismissText = stringResource(R.string.cancel_button_text),
        dialogCompleteText = stringResource(R.string.update_button_text),
        noteTitle = noteTitle,
        noteDescription = noteDescription,
        onNoteTitleChanged = { noteTitle = it },
        onNoteDescriptionChanged = { noteDescription = it },
        onDialogCompleted = { onNoteUpdated(note.id, noteTitle, noteDescription) },
        onDialogClosed = onDialogClosed
    )
}


@Preview(showBackground = true)
@Composable
fun CreateNoteDialogPreview() {
    NotesAppTheme {
        CreateNoteDialog(onDialogClosed = {}, onNoteCreated = { _, _ -> })
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateNoteDialogPreview() {
    NotesAppTheme {
        UpdateNoteDialog(
            note = Note("0", "Título Preview", "Descripción preview"),
            onDialogClosed = {},
            onNoteUpdated = { _, _, _ -> }
        )
    }
}
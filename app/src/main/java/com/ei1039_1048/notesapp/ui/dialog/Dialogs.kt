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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    label = "Título",
                    onValueChanged = onNoteTitleChanged
                )
                Spacer(modifier = Modifier.size(4.dp))
                DialogInputBox(
                    value = noteDescription,
                    label = "Descripción",
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
        dialogTitle = "Crear nueva nota",
        dialogDismissText = "Cancelar",
        dialogCompleteText = "Crear nota",
        noteTitle = noteTitle,
        noteDescription = noteDescription,
        onNoteTitleChanged = { noteTitle = it },
        onNoteDescriptionChanged = { noteDescription = it},
        onDialogCompleted = { onNoteCreated(noteTitle, noteDescription) },
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
package com.ei1039_1048.notesapp.ui.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ei1039_1048.notesapp.R
import com.ei1039_1048.notesapp.data.Note
import com.ei1039_1048.notesapp.ui.theme.NotesAppTheme

@Composable
fun NoteItemsList(
    modifier: Modifier = Modifier,
    notes: List<Note> = emptyList(),
    onEditNoteTap: (Note) -> Unit,
    onDeleteNoteTap: (Note) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(items = notes, key = { note -> note.id}) {
            NoteItemCard(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                note = it,
                onEditTap = onEditNoteTap,
                onDeleteTap = onDeleteNoteTap
            )
        }
    }
}

@Composable
fun EmptyNoteItemsList(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.empty_note_list_text),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(100.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 10.dp,
            strokeCap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteItemsListPreview() {
    NotesAppTheme {
        NoteItemsList(
            notes = listOf(Note("0", "Title", "Description"), Note("1", "Title 2", "Description")),
            onEditNoteTap = {},
            onDeleteNoteTap = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyItemsListPreview() {
    NotesAppTheme {
        EmptyNoteItemsList()
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingIndicatorPreview() {
    NotesAppTheme {
        LoadingIndicator()
    }
}
package com.ei1039_1048.notesapp.data

import com.ei1039_1048.notesapp.data.local.LocalNote
import java.time.LocalDate

fun Note.toLocal() = LocalNote(
    id = id,
    title = title,
    description = description,
    date = date.toString()
)

fun LocalNote.toUI() = Note(
    id = id,
    title = title,
    description = description,
    date = LocalDate.parse(date)
)
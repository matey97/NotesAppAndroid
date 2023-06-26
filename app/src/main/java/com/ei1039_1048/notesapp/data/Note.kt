package com.ei1039_1048.notesapp.data

import java.time.LocalDate
import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var description: String,
    val date: LocalDate = LocalDate.now()
)
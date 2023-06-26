package com.ei1039_1048.notesapp.exceptions

import java.lang.Exception

class NoteNotFoundException(
    id: String,
    message: String = "Note with id=$id not found"
) : Exception(message)
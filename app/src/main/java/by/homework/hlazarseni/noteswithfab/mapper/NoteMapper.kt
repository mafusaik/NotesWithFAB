package by.homework.hlazarseni.noteswithfab.mapper

import by.homework.hlazarseni.noteswithfab.model.NoteDTO
import by.homework.hlazarseni.noteswithfab.model.Note


internal fun List<NoteDTO>.toDomainModels(): List<Note> = map {
    it.toDomain()
}

internal fun NoteDTO.toDomain(): Note {
    return Note(
        id = id,
        title = title,
        description = description,
        time = time,
        date = date
    )
}


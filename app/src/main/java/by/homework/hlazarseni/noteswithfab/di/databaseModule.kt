package by.homework.hlazarseni.noteswithfab.di

import androidx.room.Room
import by.homework.hlazarseni.noteswithfab.database.NoteDatabase
import org.koin.dsl.module

internal val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            NoteDatabase::class.java,
            "note-database"
        )
            .allowMainThreadQueries()
            .build()
    }
    single { get<NoteDatabase>().noteDao }
}
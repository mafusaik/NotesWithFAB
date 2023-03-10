package by.homework.hlazarseni.noteswithfab.database

import androidx.room.Database
import androidx.room.RoomDatabase
import by.homework.hlazarseni.noteswithfab.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
}
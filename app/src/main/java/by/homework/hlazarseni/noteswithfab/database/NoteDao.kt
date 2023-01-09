package by.homework.hlazarseni.noteswithfab.database

import androidx.room.*
import by.homework.hlazarseni.noteswithfab.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * from Note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * from Note")
    fun getAllNotesFlow(): List<Note>

    @Query("SELECT * from Note WHERE id = :id")
    fun getNote(id: Int): Note

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: Note)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(items: List<Note>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: Note)

    @Delete
    fun delete(item: Note)
}
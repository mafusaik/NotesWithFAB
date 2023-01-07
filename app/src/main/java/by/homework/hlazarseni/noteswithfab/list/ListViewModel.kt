package by.homework.hlazarseni.noteswithfab.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import by.homework.hlazarseni.noteswithfab.database.Note
import by.homework.hlazarseni.noteswithfab.database.NoteDao
import kotlinx.coroutines.launch

class ListViewModel(private val noteDao: NoteDao): ViewModel() {

    val allCats: LiveData<List<Note>> = noteDao.getAllNotes().asLiveData()

    fun createNote(): Note {
        return Note(
            title = "Новая заметка",
            description = "",
            date = ""
        )
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }

}
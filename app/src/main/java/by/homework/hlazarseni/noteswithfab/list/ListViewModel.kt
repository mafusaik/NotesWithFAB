package by.homework.hlazarseni.noteswithfab.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import by.homework.hlazarseni.noteswithfab.model.Lce
import by.homework.hlazarseni.noteswithfab.api.BinApi
import by.homework.hlazarseni.noteswithfab.model.Note
import by.homework.hlazarseni.noteswithfab.database.NoteDao
import by.homework.hlazarseni.noteswithfab.mapper.toDomainModels
import by.homework.hlazarseni.noteswithfab.utils.currentDate
import by.homework.hlazarseni.noteswithfab.utils.currentTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext


class ListViewModel(
    private val noteDao: NoteDao,
    private val api: BinApi,
) : ViewModel() {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes().asLiveData()

    private val _apiFlow = MutableStateFlow<Lce<List<Note>>>(
        Lce.Loading
    )

    val apiFlow = _apiFlow
        .map {
            delay(5000)
            getFakeInfo()
                .fold(
                    onSuccess = {
                        insertNotes(it)
                        Lce.Content(it)
                    },
                    onFailure = {
                        Lce.Error(it)
                        getNotesDB().fold(
                            onSuccess = {list->
                                Lce.Content(list)
                            },
                            onFailure = {t->
                                Lce.Error(t)
                            }
                        )
                    }
                )
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    fun createNote(): Note {
        return Note(
            title = "Новая заметка",
            description = "",
            time = currentTime(),
            date = currentDate()
        )
    }

    suspend fun insertNote(note: Note) = withContext(Dispatchers.IO) {
        runCatching {
            noteDao.insert(note)
        }
    }

    private suspend fun insertNotes(notes: List<Note>) = withContext(Dispatchers.IO) {
        runCatching {
            noteDao.insertAll(notes)
        }
    }

    suspend fun deleteNote(note: Note) = withContext(Dispatchers.IO) {
        runCatching {
            noteDao.delete(note)
        }
    }

    private suspend fun getFakeInfo() = withContext(Dispatchers.IO) {
        runCatching {
            api.getFakeData("45717360").toDomainModels()
        }
    }

    private suspend fun getNotesDB() = withContext(Dispatchers.IO) {
        runCatching {
            noteDao.getAllNotesFlow()
        }
    }

    suspend fun updateNote(noteId: Int, editTitle: String, editDescription: String, time: String, date:String) =
        withContext(
            Dispatchers.IO
        ) {
            val note = Note(
                id = noteId,
                title = editTitle,
                description = editDescription,
                time = time,
                date = date
            )
            runCatching {
                noteDao.update(note)
            }
        }
}
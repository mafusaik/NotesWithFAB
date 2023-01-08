package by.homework.hlazarseni.noteswithfab.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import by.homework.hlazarseni.noteswithfab.Lce
import by.homework.hlazarseni.noteswithfab.api.BankCardDTO
import by.homework.hlazarseni.noteswithfab.api.BinApi
import by.homework.hlazarseni.noteswithfab.database.Note
import by.homework.hlazarseni.noteswithfab.database.NoteDao
import by.homework.hlazarseni.noteswithfab.utils.currentDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext


class ListViewModel(
    private val noteDao: NoteDao,
    private val api: BinApi,
) : ViewModel() {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes().asLiveData()

    //private val _apiFlow = MutableStateFlow<Lce<List<Note>>>(
    private val _apiFlow = MutableStateFlow<Lce<BankCardDTO>>(
        Lce.Loading
    )

    val apiFlow = _apiFlow
        .map {
            delay(5000)
            getInfo()
                .fold(
                    onSuccess = {
                        Lce.Content(it)
                    },
                    onFailure = {
                        Lce.Error(it)
                    }
                )
        }
//        .map {
//            getData()
//                .fold(
//                    onSuccess = {
//                        Lce.Content(it)
//                    },
//                    onFailure = {
//                        Lce.Error(it)
//                    }
//                )
//        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    fun createNote(): Note {
        return Note(
            title = "Новая заметка",
            description = "",
            date = currentDateTime()
        )
    }

    suspend fun insertNote(note: Note) = withContext(Dispatchers.IO) {
        runCatching {
            noteDao.insert(note)
        }
    }

    suspend fun deleteNote(note: Note) = withContext(Dispatchers.IO) {
        runCatching {
            noteDao.delete(note)
        }
    }

    private suspend fun getInfo() = withContext(Dispatchers.IO) {
        runCatching {
            api.getInfo("45717360")
        }
    }

    private suspend fun getData() = withContext(Dispatchers.IO) {
        runCatching {
            noteDao.getAllNotes2()
        }
    }
}
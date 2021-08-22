package com.kirillemets.kirillyemetsnotes.home

//import androidx.lifecycle.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.kirillyemetsnotes.database.NoteDatabase

class HomeScreenViewModel(database: NoteDatabase): ViewModel() {
    private val notesDao = database.notesDao()
    val allNotes = notesDao.getAll()


    fun onNoteClick(pos: Long) {

    }
}

class HomeScreenViewModelFactory(private val database: NoteDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            return HomeScreenViewModel(database = database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
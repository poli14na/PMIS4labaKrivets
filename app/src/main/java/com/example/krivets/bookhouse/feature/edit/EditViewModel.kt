package com.example.krivets.bookhouse.feature.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krivets.bookhouse.BookHouse
import com.example.krivets.bookhouse.data.BookNote
import com.example.krivets.bookhouse.data.BooksNotesRepository
import com.example.krivets.bookhouse.data.MyBook
import com.example.krivets.bookhouse.data.MyBooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

sealed interface BookState {
    data object Loading : BookState
    data class Error(val e: Exception) : BookState
    data class DisplayingNotes(val textNote: List<BookNote>, val book: MyBook?) : BookState

}




class NotesBookViewModel(
    private val notesBookRepository: BooksNotesRepository,
    private val booksRepository: MyBooksRepository,
    private val id: String
) : ViewModel() {

    private val loading = MutableStateFlow(false)



    val state: StateFlow<BookState> = combine(
        notesBookRepository.getAllItemsStreamByBookId(id),
        booksRepository.getItemStream(id),
        loading,
    ) { notes, book, loading ->
        if (loading) {
            BookState.Loading
        } else {
            BookState.DisplayingNotes(notes, book )
        }
    }

        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), BookState.Loading)

    fun deleteNote(id: String) = viewModelScope.launch {
        loading.value = true
        val note = notesBookRepository.getItemStream(id).first()
        note?.let {
            notesBookRepository.deleteItem(it)
        }
        loading.value = false
    }

    fun addANote(newNoteText: String, id: String) {
        val newUuid = UUID.randomUUID().toString()
        viewModelScope.launch {
            notesBookRepository.insertItem(
                BookNote(
                    uuid = newUuid,
                    bookId = id,
                    newNoteText,
                    System.currentTimeMillis()
                )
            )
        }
    }

    companion object Factory {
        fun create(application: BookHouse,  id: String): NotesBookViewModel {
            val notesBookRepository = application.container.notesRepository// Get the BooksNotesRepository instance
            val myBooksRepository = application.container.myBooksRepository
            return NotesBookViewModel(notesBookRepository = notesBookRepository, booksRepository = myBooksRepository, id = id).apply {
                // Initialize the view model with the provided ID
                // (if needed)
            }
        }
    }
}





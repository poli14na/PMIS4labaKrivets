package com.example.krivets.bookhouse.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.krivets.bookhouse.BookHouse
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

sealed interface HomeState {
    data object Loading : HomeState
    data class DisplayingBooks(val books: List<MyBook>) : HomeState
    data class Error(val e: Exception) : HomeState


}


class HomeViewModel(
    private val myBooksRepository: MyBooksRepository,
    private val notesBookRepository: BooksNotesRepository
) : ViewModel() {


    private val loading = MutableStateFlow(false)


    val state: StateFlow<HomeState> = combine(
        myBooksRepository.getAllItemsStream(),
        loading
    ) { books, loading ->
        if (loading) {
            HomeState.Loading

        } else {
            HomeState.DisplayingBooks(books)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeState.Loading)

    fun onClickRemove(id: String) = viewModelScope.launch {
        loading.value = true
        val book = myBooksRepository.getItemStream(id).first()
        book?.let {
            myBooksRepository.deleteItem(it)
        }
        loading.value = false

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookHouse)
                val myBooksRepository = application.container.myBooksRepository
                val notesBookRepository = application.container.notesRepository
                HomeViewModel(myBooksRepository = myBooksRepository, notesBookRepository = notesBookRepository)
            }
        }
    }


}

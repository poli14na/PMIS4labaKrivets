package com.example.krivets.bookhouse.feature.books

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.krivets.bookhouse.BookHouse
import com.example.krivets.bookhouse.data.Book

import com.example.krivets.bookhouse.data.BooksRepository
import com.example.krivets.bookhouse.data.MyBook
import com.example.krivets.bookhouse.data.MyBooksRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException

sealed interface BooksUiState {
    data class Success(val bookSearch: List<Book>) : BooksUiState
    data object Error : BooksUiState
    data object Loading : BooksUiState
}

class BooksViewModel(
    private val booksRepository: BooksRepository,
    private val myBooksRepository: MyBooksRepository,
) : ViewModel() {



    var booksUiState: BooksUiState by mutableStateOf(BooksUiState.Loading)
        private set

    private val _searchWidgetState: MutableState<SearchWidgetState> =
        mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> = _searchWidgetState

    private val _searchTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    init {
        getBooks()
    }

    fun getBooks(query: String = "search+terms", maxResults: Int = 40) {
        viewModelScope.launch {
            booksUiState = BooksUiState.Loading
            booksUiState =
                try {
                    val books = booksRepository.getBooks(query, maxResults)
                    BooksUiState.Success(books)
                } catch (e: IOException) {
                    BooksUiState.Error
                } catch (e: HttpException) {
                    BooksUiState.Error
                }
        }
    }

    fun addBookToMyBooks(book: Book) {
        viewModelScope.launch {
            try {
                val myBook = book.title?.let {
                    book.imageLink?.let { it1 ->
                        book.authors?.let { it2 ->
                            book.bookId?.let { it3 ->
                                MyBook(
                                    bookId = it3,
                                    title = it,
                                    authors = it2[0],
                                    imageUrl = it1
                                )
                            }
                        }
                    }
                }

                if (myBook != null) {
                    myBooksRepository.insertItem(myBook)
                }
            } catch (e: Exception) {
                // Обработка ошибок, если не удалось добавить книгу в MyBooks
            }
        }
    }

    fun checkBookExists(book: Book): Boolean {
        val existingBookFlow = book.bookId?.let { myBooksRepository.getItemStream(it) }

        val existingBook = runBlocking {
            existingBookFlow
                ?.map { it != null } // Преобразование значения в true, если книга существует, и false в противном случае
                ?.firstOrNull() // Получение первого значения из потока или null, если поток пуст
        }

        return existingBook ?: false // Возвращаем полученное значение или false, если книга не найдена
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookHouse)
                val booksRepository = application.container.booksRepository
                val myBooksRepository = application.container.myBooksRepository
                BooksViewModel(booksRepository = booksRepository, myBooksRepository = myBooksRepository)
            }
        }
    }
}

enum class SearchWidgetState {
    OPENED,
    CLOSED
}
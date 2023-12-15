package com.example.krivets.bookhouse.common.datasource

import com.example.krivets.bookhouse.R
import com.example.krivets.bookhouse.common.model.Book
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.util.UUID


interface BooksDataSource {

    fun getBooks(): Flow<List<Book>>

    fun getBook(id: UUID): Flow<Book?>

    suspend fun upsert(book: Book)

    suspend fun delete(id: UUID)
}

object InMemoryBooksDatasource : BooksDataSource {

    private var DefaultBooks = listOf(
        Book("It", "Stephen King", "Reading now",  R.drawable.book_image1),
        Book("Heaven's Blessing", "Mo Xiang Tong Xi", "Completed",  R.drawable.book_image2),
        Book("Master and Margarita", "M.Bulgakov", "Completed",  R.drawable.book_image3),
        Book("Harry Potter", "J.K.Rowling", "Reading now",  R.drawable.book_image4),
    )

    private val books = DefaultBooks.associateBy { it.id }.toMutableMap()

    private val _bookFlow = MutableSharedFlow<Map<UUID, Book>>(1)

    override fun getBook(id: UUID): Flow<Book?> = _bookFlow
        .asSharedFlow()
        .onStart {
            delay(1000L)
            emit(books)
        }
        .map { it[id] }

    override fun getBooks(): Flow<List<Book>> = _bookFlow
        .asSharedFlow()
        .onStart {
            delay(1000L)
            emit(books)
        }
        .map { it.values.toList() }

    override suspend fun upsert(book: Book) {
        delay(1000L)
        books[book.id] = book
        _bookFlow.emit(books)
    }

    override suspend fun delete(id: UUID) {
        delay(1000L)
        books.remove(id)
        _bookFlow.emit(books)
    }
}

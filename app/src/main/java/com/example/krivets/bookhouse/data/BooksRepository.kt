package com.example.krivets.bookhouse.data


import com.example.krivets.bookhouse.network.BookService


interface BooksRepository {
    suspend fun getBooks(query: String, maxResults: Int) : List<Book>
}

class NetworkBooksRepository(
    private val bookService: BookService
) : BooksRepository {
    override suspend fun getBooks(
        query: String,
        maxResults: Int
    ): List<Book> = bookService.bookSearch(query).items.map { items ->
        Book(
            title = items.volumeInfo?.title,
            authors  = items.volumeInfo?.authors,
            previewLink = items.volumeInfo?.previewLink,
            imageLink = items.volumeInfo?.imageLinks?.thumbnail,
            bookId = items.id
        )
    }
}
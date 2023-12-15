package com.example.krivets.bookhouse.data


import com.example.krivets.bookhouse.network.BookService


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



interface AppContainer {
    val booksRepository: BooksRepository
    val myBooksRepository: MyBooksRepository
    val notesRepository: BooksNotesRepository
}

class DefaultAppContainer(databaseMyBooks: MyBooksDatabase, databaseMyNotes: BookNotesDatabase) : AppContainer {
    private val BASE_URL = "https://www.googleapis.com/books/v1/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: BookService by lazy {
        retrofit.create(BookService::class.java)
    }

    private val myBooksDao: MyBooksDao = databaseMyBooks.dao()
    private val notesBooksDao: BookNotesDao = databaseMyNotes.dao()

    override val booksRepository: BooksRepository by lazy {
        NetworkBooksRepository(retrofitService)
    }

    override val myBooksRepository: MyBooksRepository by lazy {
        MyBooksRepositoryImpl(myBooksDao)
    }

    override val notesRepository: BooksNotesRepository by lazy {
        BooksNotesRepositoryNotes(notesBooksDao)
    }
}
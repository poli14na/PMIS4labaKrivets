package com.example.krivets.bookhouse.common.repository

import com.example.krivets.bookhouse.common.datasource.BooksDataSource
import com.example.krivets.bookhouse.common.datasource.InMemoryBooksDatasource
import com.example.krivets.bookhouse.common.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

interface BookRepository {

    fun getBooks(): Flow<List<Book>>
    fun getBook(id: UUID?): Flow<Book?>
    suspend fun upsert(book: Book)
    suspend fun delete(id: UUID)
}

object BookRepositoryImpl : BookRepository {

    private val dataSource: BooksDataSource = InMemoryBooksDatasource

    override fun getBooks(): Flow<List<Book>> = dataSource.getBooks()
    override fun getBook(id: UUID?): Flow<Book?> = id?.let { dataSource.getBook(it) } ?: flowOf(null)

    override suspend fun upsert(book: Book) = dataSource.upsert(book)
    override suspend fun delete(id: UUID) = dataSource.delete(id)

}

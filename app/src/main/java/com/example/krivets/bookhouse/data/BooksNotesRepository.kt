package com.example.krivets.bookhouse.data


import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface BooksNotesRepository {
    fun getAllItemsStream(): Flow<List<BookNote>>

    fun getAllItemsStreamByBookId(movieId: String): Flow<List<BookNote>>

    fun getItemStream(id: String): Flow<BookNote?>

    suspend fun insertItem(item: BookNote)

    suspend fun deleteItem(item: BookNote)

    suspend fun updateItem(item: BookNote)
}

class BooksNotesRepositoryNotes(private val booksNotesDao: BookNotesDao) : BooksNotesRepository {
    override fun getAllItemsStream(): Flow<List<BookNote>> = booksNotesDao.getAllItems()

    override fun getAllItemsStreamByBookId(
        bookId: String
    ): Flow<List<BookNote>> = booksNotesDao.getAllItemsByBookId(bookId)

    override fun getItemStream(id: String): Flow<BookNote?> = booksNotesDao.getItem(id)


    override suspend fun insertItem(item: BookNote) = booksNotesDao.insert(item)

    override suspend fun deleteItem(item: BookNote) = booksNotesDao.delete(item)

    override suspend fun updateItem(item: BookNote) = booksNotesDao.update(item)
}

@Database(entities = [BookNote::class], version = 1)
abstract class BookNotesDatabase : RoomDatabase() {
    abstract fun dao(): BookNotesDao

    companion object {
        @Volatile
        private var Instance: BookNotesDatabase? = null
        fun getDatabase(context: Context): BookNotesDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    BookNotesDatabase::class.java,
                    "book_notes"
                ).build()
            }.also { Instance = it }
        }
    }
}

@Dao
interface BookNotesDao {
    @Insert
    suspend fun insert(item: BookNote)

    @Update
    suspend fun update(item: BookNote)

    @Delete
    suspend fun delete(item: BookNote)

    @Query("SELECT * from book_notes WHERE uuid = :id")
    fun getItem(id: String): Flow<BookNote>

    @Query("SELECT * from book_notes ORDER BY time_of_creation DESC")
    fun getAllItems(): Flow<List<BookNote>>

    @Query("SELECT * FROM book_notes WHERE book_id = :bookId ORDER BY time_of_creation DESC")
    fun getAllItemsByBookId(bookId: String): Flow<List<BookNote>>
}

@Entity(tableName = "book_notes")
data class BookNote(
    @PrimaryKey val uuid: String,
    @ColumnInfo(name = "book_id") val bookId: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "time_of_creation") val timeOfCreation: Long
)
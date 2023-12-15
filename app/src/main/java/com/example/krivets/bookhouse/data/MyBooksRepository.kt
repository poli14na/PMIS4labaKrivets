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

interface MyBooksRepository {
    fun getAllItemsStream(): Flow<List<MyBook>>

    fun getItemStream(uuid: String): Flow<MyBook?>

    suspend fun insertItem(item: MyBook)

    suspend fun deleteItem(item: MyBook)

    suspend fun updateItem(item: MyBook)
}

class MyBooksRepositoryImpl(private val myBooksDao: MyBooksDao) : MyBooksRepository {
    override fun getAllItemsStream(): Flow<List<MyBook>> = myBooksDao.getAllItems()

    override fun getItemStream(uuid: String): Flow<MyBook?> = myBooksDao.getItem(uuid)

    override suspend fun insertItem(item: MyBook) = myBooksDao.insert(item)

    override suspend fun deleteItem(item: MyBook) = myBooksDao.delete(item)

    override suspend fun updateItem(item: MyBook) = myBooksDao.update(item)
}

@Database(entities = [MyBook::class], version = 1)
abstract class MyBooksDatabase : RoomDatabase() {
    abstract fun dao(): MyBooksDao

    companion object {
        @Volatile
        private var Instance: MyBooksDatabase? = null
        fun getDatabase(context: Context): MyBooksDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    MyBooksDatabase::class.java,
                    "mybooks"
                ).build()
            }.also { Instance = it }
        }
    }
}

@Dao
interface MyBooksDao {
    @Insert
    suspend fun insert(item: MyBook)

    @Update
    suspend fun update(item: MyBook)

    @Delete
    suspend fun delete(item: MyBook)

    @Query("SELECT * from mybooks WHERE bookId = :uuid")
    fun getItem(uuid: String): Flow<MyBook?>

    @Query("SELECT * from mybooks ORDER BY bookId ASC")
    fun getAllItems(): Flow<List<MyBook>>
}

@Entity(tableName = "mybooks")
data class MyBook(
    @PrimaryKey val bookId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "authors") val authors: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String
)
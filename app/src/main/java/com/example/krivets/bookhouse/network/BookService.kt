package com.example.krivets.bookhouse.network

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.krivets.bookhouse.network.model.BookHouse
interface BookService {

    @GET("volumes")
    suspend fun bookSearch(
        @Query("q") searchQuery: String
    ): BookHouse
}
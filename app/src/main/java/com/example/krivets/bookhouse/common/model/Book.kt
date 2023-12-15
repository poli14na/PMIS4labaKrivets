package com.example.krivets.bookhouse.common.model

import java.util.UUID

data class Book(
    val name: String,
    val author: String,
    val status: String,
    // val genre: String,
    // val countOfPages: Int,
    //val lastPage: Int,
    //  val annotation: String,
    //  val notes: String,
    val image: Int,
    //val lastPage: Int,
    val id: UUID = UUID.randomUUID() // java.util.UUID, сила котлина
)
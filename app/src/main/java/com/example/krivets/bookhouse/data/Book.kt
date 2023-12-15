package com.example.krivets.bookhouse.data

data class Book(
    val title: String?,
    val authors: ArrayList<String>?,
    val previewLink: String?,
    val imageLink: String?,
    val bookId: String?
)
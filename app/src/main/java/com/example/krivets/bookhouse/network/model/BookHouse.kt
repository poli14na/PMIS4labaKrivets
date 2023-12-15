package com.example.krivets.bookhouse.network.model

import com.google.gson.annotations.SerializedName


data class BookHouse (

  @SerializedName("kind"       ) var kind       : String?          = null,
  @SerializedName("totalItems" ) var totalItems : Int?             = null,
  @SerializedName("items"      ) var items      : ArrayList<Items> = arrayListOf()

)
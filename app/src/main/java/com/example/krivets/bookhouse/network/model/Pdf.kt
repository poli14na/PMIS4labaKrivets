package com.example.krivets.bookhouse.network.model
import com.google.gson.annotations.SerializedName


data class Pdf (

  @SerializedName("isAvailable"  ) var isAvailable  : Boolean? = null,
  @SerializedName("acsTokenLink" ) var acsTokenLink : String?  = null

)
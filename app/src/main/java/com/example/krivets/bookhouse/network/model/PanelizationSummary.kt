package com.example.krivets.bookhouse.network.model

import com.google.gson.annotations.SerializedName


data class PanelizationSummary (

  @SerializedName("containsEpubBubbles"  ) var containsEpubBubbles  : Boolean? = null,
  @SerializedName("containsImageBubbles" ) var containsImageBubbles : Boolean? = null

)
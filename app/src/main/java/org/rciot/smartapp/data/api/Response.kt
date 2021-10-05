package org.rciot.smartapp.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LimbahResponse(
    @field:SerializedName("Gate1Level")
    val Gate1Level: Double? = 0.0,

    @field:SerializedName("Gate1Status")
    val Gate1Status: Double? = 0.0,

    @field:SerializedName("Pump1Status")
    val Pump1Status: Double? = 0.0,

    @field:SerializedName("TDS1")
    val TDS1: Double? = 0.0,

    @field:SerializedName("Tank1Level")
    val Tank1Level: Double? = 0.0,

    @field:SerializedName("pH1")
    val pH1: Double? = 0.0
) : Parcelable
package org.rciot.smartapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LimbahResponseC(
    @field:SerializedName("Gate 1 Level")
    val gateLevel: Double? = 0.0,

    @field:SerializedName("TDS")
    val tds: Double? = 0.0,

    @field:SerializedName("Temperature")
    val temp: Double? = 0.0,

    @field:SerializedName("PH")
    val pH: Double? = 0.0
) : Parcelable

@Parcelize
data class LimbahResponseB(
    @field:SerializedName("Gate 1 Level")
    val gateLevel: Double? = 0.0,

    @field:SerializedName("TDS")
    val tds: Double? = 0.0,

    @field:SerializedName("Temperature")
    val temp: Double? = 0.0,

    @field:SerializedName("PH")
    val pH: Double? = 0.0
) : Parcelable

@Parcelize
data class LimbahResponseA(
    @field:SerializedName("Gate 1 Level")
    val gateLevel: Double? = 0.0,

    @field:SerializedName("TDS")
    val tds: Double? = 0.0,

    @field:SerializedName("Temperature")
    val temp: Double? = 0.0,

    @field:SerializedName("PH")
    val pH: Double? = 0.0
) : Parcelable

@Parcelize
data class LimbahListResponse(
    @field:SerializedName("m2m:uril")
    val dataList: List<String>?
) : Parcelable

data class DeviceData(
    @field:SerializedName("m2m:cin")
    val data: M2mCin
)

@Parcelize
data class M2mCin(
    val cnf: String? = "",
    val con: String? = "",
    val cs: Int? = 0,
    val ct: String? = "",
    val lt: String? = "",
    val pi: String? = "",
    val ri: String? = "",
    val rn: String? = "",
    val st: Int? = 0,
    val ty: Int? = 0
) : Parcelable
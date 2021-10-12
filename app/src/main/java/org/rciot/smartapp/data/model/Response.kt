package org.rciot.smartapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LimbahResponseC(
    @field:SerializedName("Gate3Level")
    val gate3Level: Double? = 0.0,

    @field:SerializedName("Gate3Status")
    val gate3Status: Double? = 0.0,

    @field:SerializedName("Pump3Status")
    val pump3Status: Double? = 0.0,

    @field:SerializedName("TDS3")
    val tds3: Double? = 0.0,

    @field:SerializedName("Tank3Level")
    val tank3Level: Double? = 0.0,

    @field:SerializedName("pH3")
    val pH3: Double? = 0.0
) : Parcelable

@Parcelize
data class LimbahResponseB(
    @field:SerializedName("Gate2Level")
    val gate2Level: Double? = 0.0,

    @field:SerializedName("Gate2Status")
    val gate2Status: Double? = 0.0,

    @field:SerializedName("Pump2Status")
    val pump2Status: Double? = 0.0,

    @field:SerializedName("TDS2")
    val tds2: Double? = 0.0,

    @field:SerializedName("Tank2Level")
    val tank2Level: Double? = 0.0,

    @field:SerializedName("pH2")
    val pH2: Double? = 0.0
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
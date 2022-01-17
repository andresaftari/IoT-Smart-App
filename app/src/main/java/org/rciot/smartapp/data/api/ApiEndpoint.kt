package org.rciot.smartapp.data.api

import org.rciot.smartapp.data.*
import org.rciot.smartapp.data.model.DeviceData
import org.rciot.smartapp.data.model.LimbahListResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiEndpoint {
    // Environment
    @GET(ENV_A_LIST_ENDPOINT)
    @Headers(
        "X-M2M-Origin: $AUTH",
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getEnvironmentAList(): LimbahListResponse

    @GET(ENV_B_LIST_ENDPOINT)
    @Headers(
        "X-M2M-Origin: $AUTH",
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getEnvironmentBList(): LimbahListResponse

    @GET(ENV_C_LIST_ENDPOINT)
    @Headers(
        "X-M2M-Origin: $AUTH",
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getEnvironmentCList(): LimbahListResponse

    // Device Data
    @GET("~{device-id}")
    @Headers(
        "X-M2M-Origin: $AUTH",
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getDeviceData(
        @Path(value = "device-id", encoded = true) deviceId: String
    ): DeviceData
}
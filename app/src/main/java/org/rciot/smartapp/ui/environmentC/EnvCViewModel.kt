package org.rciot.smartapp.ui.environmentC

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.rciot.smartapp.data.ApiStatus
import org.rciot.smartapp.data.api.EnvironmentApiConfig
import org.rciot.smartapp.data.model.DeviceData
import org.rciot.smartapp.data.model.LimbahListResponse

class EnvCViewModel : ViewModel() {
    private val _data = MutableLiveData<LimbahListResponse>()
    val data: LiveData<LimbahListResponse>
        get() = _data

    private val _deviceData = MutableLiveData<DeviceData>()
    val deviceData: LiveData<DeviceData>
        get() = _deviceData

    private val _status = MutableLiveData<ApiStatus>()
    private val apiService = EnvironmentApiConfig().service

    private suspend fun requestDataSetC() = try {
        _status.postValue(ApiStatus.LOADING)
        _data.postValue(apiService.getEnvironmentCList())
        _status.postValue(ApiStatus.SUCCESS)
    } catch (ex: Exception) {
        Log.d("Dataset_C", ex.localizedMessage!!)
        _status.postValue(ApiStatus.FAILED)
    }

    private suspend fun requestDeviceData(deviceId: String) = try {
        _status.postValue(ApiStatus.LOADING)
        _deviceData.postValue(apiService.getDeviceData(deviceId))
        _status.postValue(ApiStatus.SUCCESS)
    } catch (ex: Exception) {
        Log.d("DeviceData_C", ex.localizedMessage!!)
        _status.postValue(ApiStatus.FAILED)
    }

    fun getStatus() = _status
    fun getDeviceData(deviceId: String) = viewModelScope.launch { requestDeviceData(deviceId) }
    fun getDatasetC() = viewModelScope.launch { requestDataSetC() }
}
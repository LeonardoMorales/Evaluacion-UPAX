package dev.leonardom.evaluacionupax.feature_location.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.leonardom.evaluacionupax.core.util.DataState
import dev.leonardom.evaluacionupax.feature_location.domain.use_case.GetLastLocationUseCase
import dev.leonardom.evaluacionupax.feature_location.data.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LocationViewModel
@Inject
constructor(
    private val getLastLocationUseCase: GetLastLocationUseCase
): ViewModel() {

    private var _lastLocation = MutableStateFlow<Location?>(null)
    val lastLocation: StateFlow<Location?> = _lastLocation

    init {
        getLastLocation()
    }

    fun getLastLocation() {
        getLastLocationUseCase.invoke().onEach { dataState ->
            when(dataState){
                is DataState.Data -> {
                    dataState.data?.let { location ->
                        Log.d("LocationViewModel", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                        _lastLocation.value = location
                    }
                }
                is DataState.Loading -> Unit
                is DataState.Response -> Unit
            }
        }.launchIn(viewModelScope)
    }

}
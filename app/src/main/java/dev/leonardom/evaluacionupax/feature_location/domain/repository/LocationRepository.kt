package dev.leonardom.evaluacionupax.feature_location.domain.repository

import dev.leonardom.evaluacionupax.core.util.DataState
import dev.leonardom.evaluacionupax.feature_location.data.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getLastLocation(): Flow<DataState<Location>>

}
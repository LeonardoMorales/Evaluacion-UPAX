package dev.leonardom.evaluacionupax.feature_location.domain.use_case

import dev.leonardom.evaluacionupax.core.util.DataState
import dev.leonardom.evaluacionupax.feature_location.domain.repository.LocationRepository
import dev.leonardom.evaluacionupax.feature_location.data.model.Location
import kotlinx.coroutines.flow.Flow

class GetLastLocationUseCase(
    private val repository: LocationRepository
) {

    operator fun invoke(): Flow<DataState<Location>> {
        return repository.getLastLocation()
    }

}
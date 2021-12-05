package dev.leonardom.evaluacionupax.feature_location.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import dev.leonardom.evaluacionupax.core.util.Constants
import dev.leonardom.evaluacionupax.core.util.DataState
import dev.leonardom.evaluacionupax.core.util.ProgressBarState
import dev.leonardom.evaluacionupax.core.util.UIComponent
import dev.leonardom.evaluacionupax.feature_location.domain.repository.LocationRepository
import dev.leonardom.evaluacionupax.feature_location.data.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class LocationRepositoryImpl(
    private val locationList: CollectionReference
): LocationRepository {

    override fun getLastLocation(): Flow<DataState<Location>> = flow {
        emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

        try {
            val lastLocation = locationList
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects(Location::class.java)
                .last()

            emit(DataState.Data(data = lastLocation))

        } catch (e: Exception) {
            emit(
                DataState.Response(
                    uiComponent = UIComponent.Dialog(
                        title = "Error",
                        description = e.localizedMessage ?: Constants.UNKNOWN_ERROR
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}
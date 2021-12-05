package dev.leonardom.evaluacionupax.feature_photos.data.repository

import com.google.firebase.storage.StorageReference
import dev.leonardom.evaluacionupax.core.util.Constants
import dev.leonardom.evaluacionupax.core.util.DataState
import dev.leonardom.evaluacionupax.core.util.ProgressBarState
import dev.leonardom.evaluacionupax.core.util.UIComponent
import dev.leonardom.evaluacionupax.feature_photos.domain.repository.PhotosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class PhotosRepositoryImpl(
    private val imagesRef: StorageReference
): PhotosRepository {

    override fun uploadImage(
        data: ByteArray
    ): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading<Unit>(progressBarState = ProgressBarState.Loading))

        try {
            imagesRef
                .child("IMAGE_${System.currentTimeMillis()}")
                .putBytes(data)
                .await()

        } catch (e: Exception) {
            emit(
                DataState.Response<Unit>(
                    uiComponent = UIComponent.Dialog(
                        title = "Error",
                        description = e.localizedMessage ?: Constants.UNKNOWN_ERROR
                    )
                )
            )
        } finally {
            emit(DataState.Loading<Unit>(progressBarState = ProgressBarState.Idle))
        }
    }
}
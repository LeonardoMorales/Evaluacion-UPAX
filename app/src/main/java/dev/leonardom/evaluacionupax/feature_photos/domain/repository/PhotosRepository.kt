package dev.leonardom.evaluacionupax.feature_photos.domain.repository

import android.graphics.drawable.BitmapDrawable
import dev.leonardom.evaluacionupax.core.util.DataState
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {

    fun uploadImage(data: ByteArray): Flow<DataState<Unit>>

}
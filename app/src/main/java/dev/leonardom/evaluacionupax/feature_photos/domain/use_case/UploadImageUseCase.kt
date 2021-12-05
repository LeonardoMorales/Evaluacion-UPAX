package dev.leonardom.evaluacionupax.feature_photos.domain.use_case

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import dev.leonardom.evaluacionupax.R
import dev.leonardom.evaluacionupax.core.util.DataState
import dev.leonardom.evaluacionupax.core.util.UIComponent
import dev.leonardom.evaluacionupax.feature_photos.domain.repository.PhotosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayOutputStream

class UploadImageUseCase(
    private val repository: PhotosRepository
){

    operator fun invoke(
        drawable: Drawable?
    ): Flow<DataState<Unit>> {

        if(drawable == null) {
            return flow {
                emit(
                    DataState.Response(
                        uiComponent = UIComponent.Dialog(
                            title = "Seleccione una imágen",
                            description = "Para guardar la imágen primero seleccione una desde la cámara o la galería"
                        )
                    )
                )
            }
        }

        val bitmap = (drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        return repository.uploadImage(data)
    }

}
package dev.leonardom.evaluacionupax.feature_photos.presentation

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.leonardom.evaluacionupax.core.util.DataState
import dev.leonardom.evaluacionupax.core.util.ProgressBarState
import dev.leonardom.evaluacionupax.core.util.Queue
import dev.leonardom.evaluacionupax.core.util.UIComponent
import dev.leonardom.evaluacionupax.feature_photos.domain.use_case.UploadImageUseCase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel
@Inject
constructor(
    private val uploadImageUseCase: UploadImageUseCase
): ViewModel() {

    private var _cameraTmpUri = MutableStateFlow<Uri?>(null)
    val cameraTmpUri: StateFlow<Uri?> = _cameraTmpUri

    private var _displayProgressBar = MutableStateFlow<ProgressBarState>(ProgressBarState.Idle)
    val displayProgressBar: StateFlow<ProgressBarState> = _displayProgressBar

    private var _errorMessageQueue = MutableStateFlow<Queue<UIComponent>>(Queue(mutableListOf()))
    val errorMessageQueue: StateFlow<Queue<UIComponent>> = _errorMessageQueue

    fun updateTmpUri(newUri: Uri) {
        _cameraTmpUri.value = newUri
    }

    fun uploadImage(drawable: Drawable?) {
        uploadImageUseCase.invoke(drawable).onEach { dataState ->
            when(dataState){
                is DataState.Data -> Unit
                is DataState.Loading -> {
                    _displayProgressBar.value = dataState.progressBarState
                }
                is DataState.Response -> {
                    when(dataState.uiComponent){
                        is UIComponent.Dialog -> {
                            Log.d("PhotosViewModel", "Response: ${dataState.uiComponent.description}")
                            appendToMessageQueue(dataState.uiComponent)
                        }
                        is UIComponent.None -> {
                            Log.d("PhotosViewModel", "Response: ${dataState.uiComponent.message}")
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun appendToMessageQueue(uiComponent: UIComponent){
        val queue = _errorMessageQueue.value
        queue.add(uiComponent)
        _errorMessageQueue.value = Queue(mutableListOf())
        _errorMessageQueue.value = queue
    }

    fun removeHeadMessage() {
        try {
            val queue = _errorMessageQueue.value
            queue.remove() // Se lanza una excepción si esta vacia
            _errorMessageQueue.value = Queue(mutableListOf())
            _errorMessageQueue.value = queue
        }catch (e: Exception){
            Log.d("PhotosViewModel","Nothing to remove from DialogQueue")
        }
    }

}
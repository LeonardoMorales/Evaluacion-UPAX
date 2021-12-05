package dev.leonardom.evaluacionupax.feature_movies.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.leonardom.evaluacionupax.core.domain.model.Movie
import dev.leonardom.evaluacionupax.core.util.DataState
import dev.leonardom.evaluacionupax.core.util.ProgressBarState
import dev.leonardom.evaluacionupax.core.util.Queue
import dev.leonardom.evaluacionupax.core.util.UIComponent
import dev.leonardom.evaluacionupax.feature_movies.domain.use_case.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel
@Inject
constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
): ViewModel() {

    private var _movieList = MutableStateFlow<List<Movie>>(emptyList())
    val movieList: StateFlow<List<Movie>> = _movieList

    private var _displayProgressBar = MutableStateFlow<ProgressBarState>(ProgressBarState.Idle)
    val displayProgressBar: StateFlow<ProgressBarState> = _displayProgressBar

    private var _errorMessageQueue = MutableStateFlow<Queue<UIComponent>>(Queue(mutableListOf()))
    val errorMessageQueue: StateFlow<Queue<UIComponent>> = _errorMessageQueue

    init {
        getPopularMovies()
    }

    private fun getPopularMovies() {
        getPopularMoviesUseCase.invoke().onEach { dataState ->
            when(dataState){
                is DataState.Data -> {
                    dataState.data?.let { movies ->
                        _movieList.value = movies
                    }
                }
                is DataState.Loading -> {
                    _displayProgressBar.value = dataState.progressBarState
                }
                is DataState.Response -> {
                    when(dataState.uiComponent){
                        is UIComponent.Dialog -> {
                            Log.d("MoviesViewModel", "Response: ${dataState.uiComponent.description}")
                            appendToMessageQueue(dataState.uiComponent)
                        }
                        is UIComponent.None -> {
                            Log.d("MoviesViewModel", "Response: ${dataState.uiComponent.message}")
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
            Log.d("MoviesViewModel","Nothing to remove from DialogQueue")
        }
    }

}
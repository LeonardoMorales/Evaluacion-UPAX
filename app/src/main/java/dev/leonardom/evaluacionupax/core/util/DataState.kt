package dev.leonardom.evaluacionupax.core.util

/*
    SealedClass para el manejo de los diferentes resultados de operaciones con internet
    y base de datos
 */
sealed class DataState<T> {
    data class Response<T>(
        val uiComponent: UIComponent
    ): DataState<T>()

    data class Data<T>(
        val data: T? = null
    ): DataState<T>()

    data class Loading<T>(
        val progressBarState: ProgressBarState = ProgressBarState.Idle
    ): DataState<T>()
}

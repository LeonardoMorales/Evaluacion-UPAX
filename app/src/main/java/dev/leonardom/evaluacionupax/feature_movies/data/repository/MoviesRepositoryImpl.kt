package dev.leonardom.evaluacionupax.feature_movies.data.repository

import dev.leonardom.evaluacionupax.core.domain.model.Movie
import dev.leonardom.evaluacionupax.core.util.Constants
import dev.leonardom.evaluacionupax.core.util.DataState
import dev.leonardom.evaluacionupax.core.util.ProgressBarState
import dev.leonardom.evaluacionupax.core.util.UIComponent
import dev.leonardom.evaluacionupax.feature_movies.data.cache.movie.MovieDao
import dev.leonardom.evaluacionupax.feature_movies.data.network.MoviesApi
import dev.leonardom.evaluacionupax.feature_movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class MoviesRepositoryImpl(
    private val api: MoviesApi,
    private val cache: MovieDao
): MoviesRepository {

    override fun getPopularMovies(
        sortBy: String
    ): Flow<DataState<List<Movie>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            val movies: List<Movie> = try {
                api.getMovies(sortBy).results.map { it.toMovie() }
            } catch (e: Exception){
                e.printStackTrace() // log to crashlytics?
                emit(DataState.Response<List<Movie>>(
                    uiComponent = UIComponent.Dialog(
                        title = "Network Data Error",
                        description = e.message?: "Unknown error"
                    )
                ))
                listOf()
            }

            // almacenar resultado en la base de datos
            cache.insertMovies(movies.map { it.toMovieEntity() })

            // obtener lista de peliculas de la base de datos
            val cachedMovies = cache.getAllMovies().map { it.toMovie() }

            emit(DataState.Data(cachedMovies))

        } catch(e: Exception) {
            e.printStackTrace()
            val error = e.message ?: Constants.UNKNOWN_ERROR

            emit(
                DataState.Response(
                    uiComponent = UIComponent.Dialog(
                        title = "Error",
                        description = error
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}
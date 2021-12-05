package dev.leonardom.evaluacionupax.feature_movies.data.network

import dev.leonardom.evaluacionupax.core.util.Constants
import dev.leonardom.evaluacionupax.feature_movies.data.network.dto.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

/*
    Configuración de Servicio API
    Aquí se alojarán las diferentes peticiones a los endpoints de la API TheMovieDB
    https://www.themoviedb.org/documentation/api
 */
interface MoviesApi {

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("sort_by") sortBy: String,
    ): MovieResponse

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

}
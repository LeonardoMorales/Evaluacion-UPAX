package dev.leonardom.evaluacionupax.feature_movies.domain.repository

import dev.leonardom.evaluacionupax.core.domain.model.Movie
import dev.leonardom.evaluacionupax.core.util.DataState
import dev.leonardom.evaluacionupax.feature_movies.data.network.dto.MovieResponse
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getPopularMovies(sortBy: String): Flow<DataState<List<Movie>>>

}
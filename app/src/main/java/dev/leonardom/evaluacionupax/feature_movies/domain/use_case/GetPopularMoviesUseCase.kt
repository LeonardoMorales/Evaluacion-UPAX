package dev.leonardom.evaluacionupax.feature_movies.domain.use_case

import dev.leonardom.evaluacionupax.core.domain.model.Movie
import dev.leonardom.evaluacionupax.core.util.DataState
import dev.leonardom.evaluacionupax.feature_movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetPopularMoviesUseCase(
    private val repository: MoviesRepository
) {

    operator fun invoke(): Flow<DataState<List<Movie>>> {
        return repository.getPopularMovies(sortBy = "popularity.desc")
    }

}
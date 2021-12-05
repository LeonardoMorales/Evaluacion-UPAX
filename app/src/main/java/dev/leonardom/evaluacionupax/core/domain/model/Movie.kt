package dev.leonardom.evaluacionupax.core.domain.model

import dev.leonardom.evaluacionupax.feature_movies.data.cache.movie.MovieEntity

/*
    MODELO DE INFORMACIÓN DE PELÍCULA
 */
data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val id: Int,
    val overview: String,
    val popularity: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Float,
){
    // Transformar Entidad de Película a Modelo de Película
    fun toMovieEntity(): MovieEntity {
        return MovieEntity(
            id = id,
            adult = adult,
            backdrop_path = backdrop_path,
            overview = overview,
            popularity = popularity,
            poster_path = poster_path,
            release_date = release_date,
            title = title,
            vote_average = vote_average
        )
    }
}

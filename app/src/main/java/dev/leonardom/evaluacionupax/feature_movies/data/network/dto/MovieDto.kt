package dev.leonardom.evaluacionupax.feature_movies.data.network.dto

import dev.leonardom.evaluacionupax.core.domain.model.Movie

/*
    Data Transfer Object para Modelo de Película
 */
data class MovieDto(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Float,
    val vote_count: Int,
){
    // Convertir DTO a Modelo de película
    fun toMovie(): Movie {
        return Movie(
            adult = adult,
            backdrop_path = backdrop_path,
            id = id,
            overview = overview,
            popularity = popularity,
            poster_path = poster_path,
            release_date = release_date,
            title = title,
            vote_average = vote_average
        )
    }
}
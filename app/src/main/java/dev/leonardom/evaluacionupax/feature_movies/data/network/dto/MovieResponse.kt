package dev.leonardom.evaluacionupax.feature_movies.data.network.dto

/*
    Modelo de Response de MovieAPI
 */
data class MovieResponse(
    val page: Int,
    val results: List<MovieDto>
)

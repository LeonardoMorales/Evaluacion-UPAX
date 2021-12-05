package dev.leonardom.evaluacionupax.feature_movies.data.cache.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.leonardom.evaluacionupax.core.domain.model.Movie

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val adult: Boolean,
    val backdrop_path: String,
    val overview: String,
    val popularity: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Float,
){
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

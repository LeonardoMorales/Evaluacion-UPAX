package dev.leonardom.evaluacionupax.feature_movies.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.leonardom.evaluacionupax.feature_movies.data.cache.movie.MovieDao
import dev.leonardom.evaluacionupax.feature_movies.data.cache.movie.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object {
        val DATABASE_NAME = "movie_db"
    }

}
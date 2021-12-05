package dev.leonardom.evaluacionupax.feature_movies.data.cache.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>): List<Long>

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>

}
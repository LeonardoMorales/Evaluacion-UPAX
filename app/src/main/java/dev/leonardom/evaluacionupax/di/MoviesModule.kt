package dev.leonardom.evaluacionupax.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.leonardom.evaluacionupax.feature_movies.data.cache.AppDatabase
import dev.leonardom.evaluacionupax.feature_movies.data.cache.movie.MovieDao
import dev.leonardom.evaluacionupax.feature_movies.data.network.MoviesApi
import dev.leonardom.evaluacionupax.feature_movies.data.repository.MoviesRepositoryImpl
import dev.leonardom.evaluacionupax.feature_movies.domain.repository.MoviesRepository
import dev.leonardom.evaluacionupax.feature_movies.domain.use_case.GetPopularMoviesUseCase
import dev.leonardom.evaluacionupax.feature_movies.presentation.MovieListAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoviesModule {

    @Provides
    @Singleton
    fun provideMoviesApi(client: OkHttpClient): MoviesApi {
        return Retrofit.Builder()
            .baseUrl(MoviesApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(
        db: AppDatabase
    ): MovieDao {
        return db.getMovieDao()
    }

    @Provides
    @Singleton
    fun provideMoviesRepository(
        api: MoviesApi,
        dao: MovieDao
    ): MoviesRepository {
        return MoviesRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideGetPopularMoviesUseCase(repository: MoviesRepository): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideMovieListAdapter() = MovieListAdapter()

}
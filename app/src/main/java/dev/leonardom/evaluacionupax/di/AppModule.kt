package dev.leonardom.evaluacionupax.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.leonardom.evaluacionupax.core.util.Constants.API_KEY
import dev.leonardom.evaluacionupax.feature_movies.data.cache.AppDatabase
import dev.leonardom.evaluacionupax.feature_movies.data.cache.AppDatabase.Companion.DATABASE_NAME
import dev.leonardom.evaluacionupax.feature_movies.data.cache.movie.MovieDao
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val httpUrl = original.url

                val newHttpUrl = httpUrl.newBuilder()
                    .addQueryParameter(
                        name = "api_key",
                        value = API_KEY
                    )
                    .build()

                val request = original.newBuilder().url(newHttpUrl).build()

                return@addInterceptor it.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideFirestoreInstance() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideStorageInstance() = FirebaseStorage.getInstance()
}
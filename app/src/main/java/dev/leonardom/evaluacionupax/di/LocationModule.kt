package dev.leonardom.evaluacionupax.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.leonardom.evaluacionupax.feature_location.data.repository.LocationRepositoryImpl
import dev.leonardom.evaluacionupax.feature_location.domain.repository.LocationRepository
import dev.leonardom.evaluacionupax.feature_location.domain.use_case.GetLastLocationUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun provideLocationList(
        firestore: FirebaseFirestore
    ) = firestore.collection("location")

    @Provides
    @Singleton
    fun provideLocationRepository(
        locationList: CollectionReference
    ): LocationRepository {
        return LocationRepositoryImpl(locationList)
    }

    @Provides
    @Singleton
    fun provideGetLasLocationUseCase(repository: LocationRepository): GetLastLocationUseCase {
        return GetLastLocationUseCase(repository)
    }

}
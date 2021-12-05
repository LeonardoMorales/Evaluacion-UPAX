package dev.leonardom.evaluacionupax.di

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.leonardom.evaluacionupax.feature_photos.data.repository.PhotosRepositoryImpl
import dev.leonardom.evaluacionupax.feature_photos.domain.repository.PhotosRepository
import dev.leonardom.evaluacionupax.feature_photos.domain.use_case.UploadImageUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhotosModule {

    @Provides
    @Singleton
    fun provideImagesReference(
        storage: FirebaseStorage
    ) = storage.reference.child("images")

    @Provides
    @Singleton
    fun providePhotosRepository(
        imagesReference: StorageReference
    ): PhotosRepository {
        return PhotosRepositoryImpl(imagesReference)
    }

    @Provides
    @Singleton
    fun provideUploadImageUseCase(repository: PhotosRepository): UploadImageUseCase {
        return UploadImageUseCase(repository)
    }

}
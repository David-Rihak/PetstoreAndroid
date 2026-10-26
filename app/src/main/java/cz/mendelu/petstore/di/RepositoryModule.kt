package cz.mendelu.petstore.di

import cz.mendelu.petstore.communication.IPetsRemoteRepository
import cz.mendelu.petstore.communication.PetsAPI
import cz.mendelu.petstore.communication.PetsRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePetsRemoteRepository(petsAPI: PetsAPI): IPetsRemoteRepository =
        PetsRemoteRepositoryImpl(petsAPI)
}
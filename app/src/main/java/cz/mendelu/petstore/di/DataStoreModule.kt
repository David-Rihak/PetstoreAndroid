package cz.mendelu.petstore.di

import android.content.Context
import cz.mendelu.petstore.dataStore.DataStoreRepositoryImpl
import cz.mendelu.petstore.dataStore.IDataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context): IDataStoreRepository =
        DataStoreRepositoryImpl(context)
}
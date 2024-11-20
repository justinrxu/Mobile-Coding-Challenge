package com.example.mobilecodingchallenge.domain.di

import com.example.mobilecodingchallenge.domain.repository.ImageSearchRepositoryImpl
import com.example.mobilecodingchallenge.domain.repository.interfaces.IImageSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds
    abstract fun provideImageSearchRepository(
        itemRepositoryImpl: ImageSearchRepositoryImpl
    ): IImageSearchRepository
}
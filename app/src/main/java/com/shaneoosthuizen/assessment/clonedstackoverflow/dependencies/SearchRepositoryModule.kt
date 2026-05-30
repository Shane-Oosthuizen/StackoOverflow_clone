package com.shaneoosthuizen.assessment.clonedstackoverflow.dependencies

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.data.SearchRepositoryImpl
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        impl: SearchRepositoryImpl
    ): SearchRepository
}
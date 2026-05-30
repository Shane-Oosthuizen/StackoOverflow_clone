package com.shaneoosthuizen.assessment.clonedstackoverflow.dependencies

import com.shaneoosthuizen.assessment.clonedstackoverflow.core.cache.OfflineCacheRepository
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.cache.OfflineCacheRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OfflineCacheRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCacheRepository(impl: OfflineCacheRepositoryImpl): OfflineCacheRepository
}
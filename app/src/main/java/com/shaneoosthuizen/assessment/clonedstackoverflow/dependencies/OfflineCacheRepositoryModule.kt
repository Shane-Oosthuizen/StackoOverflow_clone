package com.shaneoosthuizen.assessment.clonedstackoverflow.dependencies


import com.shaneoosthuizen.assessment.clonedstackoverflow.components.offlinecache.data.OfflineCacheRepositoryImpl
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.offlinecache.domain.OfflineCacheRepository

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
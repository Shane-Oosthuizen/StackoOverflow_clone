package com.shaneoosthuizen.assessment.clonedstackoverflow.dependencies

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.answer.AnswerRepositoryImpl
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.repository.AnswerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnswerRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAnswerRepository(
        impl: AnswerRepositoryImpl
    ): AnswerRepository
}
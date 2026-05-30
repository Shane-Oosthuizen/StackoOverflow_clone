package com.shaneoosthuizen.assessment.clonedstackoverflow.dependencies

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.question.QuestionRepositoryImpl
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.repository.QuestionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QuestionRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindQuestionRepository(
        impl: QuestionRepositoryImpl
    ): QuestionRepository
}
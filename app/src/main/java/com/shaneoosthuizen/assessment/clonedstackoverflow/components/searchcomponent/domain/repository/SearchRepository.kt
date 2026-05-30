package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.repository

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Search
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getQuestions(): Flow<List<Question>>
    fun searchTopic(topic: Search, ): Flow<List<Question>>
    suspend fun saveQuestionForOffline(question: Question)
}
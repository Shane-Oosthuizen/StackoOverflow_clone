package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.data

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Search
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchData: SearchDataSource
) : SearchRepository {

    override fun getQuestions(): Flow<List<Question>> {
        return searchData.getQuestions()
    }

    override fun searchTopic(topic: Search): Flow<List<Question>> {
        return searchData.searchTopic(topic)
    }

    override suspend fun saveQuestionForOffline(question: Question) {
        searchData.saveQuestionForOffline(question)
    }

}
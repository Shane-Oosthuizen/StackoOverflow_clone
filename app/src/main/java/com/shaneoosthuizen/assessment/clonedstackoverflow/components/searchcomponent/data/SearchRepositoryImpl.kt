package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.data

import androidx.compose.foundation.pager.PageSize
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Search
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchData: SearchDataSource
) : SearchRepository {

    override fun getQuestions(questionAmount: Int): Flow<List<Question>> {
        return searchData.getQuestions(questionAmount)
    }

    override fun searchTopic(topic: Search): Flow<List<Question>> {
        return searchData.searchTopic(topic)
    }

    override fun searchTag(tag: String): Flow<List<Question>> {
        return searchData.searchTag(tag)
    }


}
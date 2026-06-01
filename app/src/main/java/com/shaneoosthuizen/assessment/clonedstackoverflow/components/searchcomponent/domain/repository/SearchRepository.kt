package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.repository

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Search
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getQuestions(questionAmount : Int): Flow<List<Question>>
    fun searchTopic(topic: Search, ): Flow<List<Question>>

    fun searchTag(tag: String): Flow<List<Question>>
}
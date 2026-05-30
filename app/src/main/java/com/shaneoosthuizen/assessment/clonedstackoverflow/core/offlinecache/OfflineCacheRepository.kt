package com.shaneoosthuizen.assessment.clonedstackoverflow.core.cache

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Answer
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question

interface OfflineCacheRepository {
    suspend fun cacheQuestion(question: Question, answers: List<Answer>)
    suspend fun getCachedQuestions(): List<Question>
    suspend fun getCachedQuestion(questionId: Int): Question?
}
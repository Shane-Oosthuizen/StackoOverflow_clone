package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.answer

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.answer.models.toDomain
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Answer
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Comment
import javax.inject.Inject

class AnswerDataSource @Inject constructor(
    private val apiService: AnswerAPIService
) {
    suspend fun getAnswersForQuestion(questionId: Int): List<Answer> {
        return apiService.getAnswersForQuestion(questionId).items.map { it.toDomain() }
    }

    suspend fun getAnswerById(answerId: Int): Answer {
        return apiService.getAnswerById(answerId).items.first().toDomain()
    }

    suspend fun getCommentsForAnswer(answerId: Int): List<Comment> {
        return apiService.getCommentsForAnswer(answerId).items.map { it.toDomain() }
    }
}
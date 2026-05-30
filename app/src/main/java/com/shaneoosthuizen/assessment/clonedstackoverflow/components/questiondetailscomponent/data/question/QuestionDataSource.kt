package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.question

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.question.models.toDomain
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question
import javax.inject.Inject

class QuestionDataSource  @Inject constructor(
    private val apiService: QuestionAPIService
) {
    suspend fun getQuestionDetailsByLink(questionId: Int): Question {
        return apiService.getQuestionDetailsByLink(questionId).items.first().toDomain()
    }
}
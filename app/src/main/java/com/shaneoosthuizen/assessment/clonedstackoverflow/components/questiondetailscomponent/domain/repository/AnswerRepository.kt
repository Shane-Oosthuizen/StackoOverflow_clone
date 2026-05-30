package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.repository

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Answer
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Comment

interface AnswerRepository {
    suspend fun getAnswersForQuestion(questionId: Int): List<Answer>
    suspend fun getAnswerById(answerId: Int): Answer
    suspend fun getCommentsForAnswer(answerId: Int): List<Comment>
}
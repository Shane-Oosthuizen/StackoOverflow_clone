package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.answer

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Answer
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Comment
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.repository.AnswerRepository
import javax.inject.Inject

class AnswerRepositoryImpl @Inject constructor(
    private val answerData: AnswerDataSource
) : AnswerRepository {
    override suspend fun getAnswersForQuestion(questionId: Int): List<Answer> {
        return answerData.getAnswersForQuestion(questionId)
    }

    override suspend fun getAnswerById(answerId: Int): Answer {
        return answerData.getAnswerById(answerId)
    }

    override suspend fun getCommentsForAnswer(answerId: Int): List<Comment> {
        return answerData.getCommentsForAnswer(answerId)
    }
}
package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.question

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor (
    private val questionData : QuestionDataSource
): QuestionRepository{
    override suspend fun getQuestionDetailsByLink(link: Int): Question {
        return questionData.getQuestionDetailsByLink(link)
    }

}
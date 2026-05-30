package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.repository

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question

interface QuestionRepository {

   suspend fun getQuestionDetailsByLink(link: Int): Question
}
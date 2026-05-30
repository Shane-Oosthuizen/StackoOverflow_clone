package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.question

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.question.models.QuestionDetailsResponse
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.OrderEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.SortEnum
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuestionAPIService {
    @GET("2.3/questions/{questionId}")
    suspend fun getQuestionDetailsByLink(
        @Path("questionId") questionId: Int,
        @Query("site") site: String = "stackoverflow",
        @Query("order") order: String = OrderEnum.DESCENDING.toString(),
        @Query("sort") sort: String = SortEnum.ACTIVITY.toString(),
    ): QuestionDetailsResponse
}

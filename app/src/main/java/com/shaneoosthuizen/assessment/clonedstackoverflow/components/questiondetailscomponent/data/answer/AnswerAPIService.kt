package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.answer

import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.answer.models.AnswersResponse
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.answer.models.CommentsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnswerAPIService {
    @GET("2.3/questions/{questionId}/answers")
    suspend fun getAnswersForQuestion(
        @Path("questionId") questionId: Int,
        @Query("site") site: String = "stackoverflow",
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "activity",
        @Query("filter") filter: String = "!6WPIomqomPQfa" // custom filter created on the stack overflow docs to body and answer comment count
    ): AnswersResponse

    @GET("2.3/answers/{answerId}")
    suspend fun getAnswerById(
        @Path("answerId") answerId: Int,
        @Query("site") site: String = "stackoverflow",
        @Query("filter") filter: String = "withbody"
    ): AnswersResponse

    @GET("2.3/answers/{answerId}/comments")
    suspend fun getCommentsForAnswer(
        @Path("answerId") answerId: Int,
        @Query("site") site: String = "stackoverflow",
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "creation",
        @Query("filter") filter: String = "withbody"
    ): CommentsResponse
}



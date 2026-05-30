package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.data


import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.data.models.QuestionsResponse
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.OrderEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SortEnum
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApiService {
    @GET("2.3/questions")
    suspend fun getQuestions(
        @Query("order") order: String = OrderEnum.DESCENDING.toString(),
        @Query("sort") sort: String = SortEnum.ACTIVITY.toString(),
        @Query("site") site: String = "stackoverflow"
    ): QuestionsResponse

    @GET("/2.3/search/advanced?site=stackoverflow&filter=withbody")
    suspend fun searchTopic(
        @Query("title") searchText: String,
        @Query("sort") sortBy: String,
        @Query("order") sortOrder: String,
        @Query("pagesize") pageSize: String
    ): QuestionsResponse
}
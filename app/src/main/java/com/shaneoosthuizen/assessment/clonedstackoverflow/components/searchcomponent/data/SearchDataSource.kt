package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.data


import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.data.models.toDomain
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Search
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchDataSource @Inject constructor(
    private val apiService: SearchApiService
) {
    fun getQuestions(): Flow<List<Question>> = flow {
        try {
            val questions = apiService.getQuestions().items.map { it.toDomain() }
            emit(questions)
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", "getQuestions failed: ${e.message}")
            emit(emptyList())
        }
    }

    fun searchTopic(topic: Search): Flow<List<Question>> = flow {
        try {
            val questions = apiService.searchTopic(
                topic.SearchText,
                topic.Sort.value,   //  Changed from .name.lowercase() -> yields "activity", "votes", etc.
                topic.Order.value,  //  Changed from .name.lowercase() -> yields "asc" or "desc"
                topic.PageSize.toString()
            ).items.map { it.toDomain() }

            emit(questions)
        } catch (e: retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            android.util.Log.e("API_ERROR", "HTTP 400 Details: $errorBody")
            emit(emptyList()) // Safely recover from the error stream
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", "Generic Error: ${e.localizedMessage}")
            emit(emptyList())
        }
    }

    suspend fun saveQuestionForOffline(question: Question) {
        // TODO: implement Room persistence
    }
}

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
    fun getQuestions(questionAmount:Int): Flow<List<Question>> = flow {
        try {
            val questions = apiService.getQuestions(pageSize = questionAmount).items.map { it.toDomain() }
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
                topic.Sort.value,
                topic.Order.value,
                topic.PageSize.toString()
            ).items.map { it.toDomain() }
            emit(questions)
        } catch (e: retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            android.util.Log.e("API_ERROR", "HTTP Details: $errorBody")
            emit(emptyList())
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", "Generic Error: ${e.localizedMessage}")
            emit(emptyList())
        }
    }


     fun searchTag(tag: String): Flow<List<Question>> = flow {
       try{
           val questions = apiService.searchTag(
               tag
           ).items.map { it.toDomain() }
           emit(questions)
       }catch (e: retrofit2.HttpException) {
           val errorBody = e.response()?.errorBody()?.string()
           android.util.Log.e("API_ERROR", "HTTP Details: $errorBody")
           emit(emptyList())
       } catch (e: Exception) {
           android.util.Log.e("API_ERROR", "Generic Error: ${e.localizedMessage}")
           emit(emptyList())
       }
    }
}

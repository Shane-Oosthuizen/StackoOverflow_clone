package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Answer
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.OrderEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.SortEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.repository.AnswerRepository
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.repository.QuestionRepository
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.offlinecache.domain.OfflineCacheRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
    private val offlineCache: OfflineCacheRepository
) : ViewModel() {

    private val _question = MutableStateFlow<Question?>(null)
    val question: StateFlow<Question?> = _question

    private val _allAnswers = MutableStateFlow<List<Answer>>(emptyList())
    private val _answers = MutableStateFlow<List<Answer>>(emptyList())
    val answers: StateFlow<List<Answer>> = _answers

    private val _sortOption = MutableStateFlow(SortEnum.VOTES)
    val sortOption: StateFlow<SortEnum> = _sortOption

    private val _orderOption = MutableStateFlow(OrderEnum.DESCENDING)
    val orderOption: StateFlow<OrderEnum> = _orderOption

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isFromCache = MutableStateFlow(false)
    val isFromCache: StateFlow<Boolean> = _isFromCache

    fun updateSortOption(sort: SortEnum) {
        _sortOption.value = sort
        applySortOrder()
    }

    fun updateOrderOption(order: OrderEnum) {
        _orderOption.value = order
        applySortOrder()
    }

    private fun applySortOrder() {
        val sorted = when (_sortOption.value) {
            SortEnum.VOTES -> _allAnswers.value.sortedBy { it.score }
            SortEnum.CREATION -> _allAnswers.value.sortedBy { it.creationDate }
            SortEnum.ACTIVITY -> _allAnswers.value.sortedBy { it.creationDate }
        }
        _answers.value = if (_orderOption.value == OrderEnum.DESCENDING) sorted.reversed() else sorted
    }

    fun loadQuestion(questionId: Int) {
        if (_question.value != null) return
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            try {
                val question: Question
                val answers: List<Answer>
                coroutineScope {
                    val questionDeferred = async { questionRepository.getQuestionDetailsByLink(questionId) }
                    val answersDeferred = async { answerRepository.getAnswersForQuestion(questionId) }
                    question = questionDeferred.await()
                    answers = answersDeferred.await()
                }
                _question.value = question
                _allAnswers.value = answers
                _isFromCache.value = false
                applySortOrder()
                viewModelScope.launch(Dispatchers.IO) {
                    try { offlineCache.cacheQuestion(question, answers) }
                    catch (e: Exception) { Log.e ("QuestionViewModel", "Failed to cache question: ${e.message}") }
                }
            } catch (e: Exception) {
                try {
                    val cachedData = offlineCache.getCachedQuestion(questionId)
                    if (cachedData != null) {
                        _question.value = cachedData
                        _allAnswers.value = cachedData.answers
                        _isFromCache.value = true
                        applySortOrder()
                    } else {
                        _error.value = "No cached data available."
                    }
                } catch (cacheError: Exception) {
                    _error.value = "Failed to load question: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
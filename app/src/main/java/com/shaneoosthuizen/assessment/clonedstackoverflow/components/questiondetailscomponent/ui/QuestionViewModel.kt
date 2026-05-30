package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Answer
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.repository.AnswerRepository
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.repository.QuestionRepository
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.cache.OfflineCacheRepository
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

    private val _answers = MutableStateFlow<List<Answer>>(emptyList())
    val answers: StateFlow<List<Answer>> = _answers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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
                _answers.value = answers
                viewModelScope.launch(Dispatchers.IO) {
                    try { offlineCache.cacheQuestion(question, answers) }
                    catch (e: Exception) { /* cache write is best-effort */ }
                }
            } catch (e: Exception) {
                try {
                    val cachedData = offlineCache.getCachedQuestion(questionId)
                    if (cachedData != null) {
                        _question.value = cachedData
                        _answers.value = cachedData.answers
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
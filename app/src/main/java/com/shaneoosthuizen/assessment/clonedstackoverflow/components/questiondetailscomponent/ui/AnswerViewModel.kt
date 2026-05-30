package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Answer
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Comment
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.repository.AnswerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnswerViewModel @Inject constructor(
    private val answerRepository: AnswerRepository
) : ViewModel() {

    private val _answer = MutableStateFlow<Answer?>(null)
    val answer: StateFlow<Answer?> = _answer

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadAnswerWithComments(answerId: Int) {
        if (_answer.value != null) return
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            try {
                val answerDeferred = async { answerRepository.getAnswerById(answerId) }
                val commentsDeferred = async { answerRepository.getCommentsForAnswer(answerId) }
                _answer.value = answerDeferred.await()
                _comments.value = commentsDeferred.await()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
package com.shaneoosthuizen.assessment.clonedstackoverflow.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.cache.OfflineCacheRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineCacheViewModel @Inject constructor(
    private val cacheRepository: OfflineCacheRepository
) : ViewModel() {

    private val _cachedQuestions = MutableStateFlow<List<Question>>(emptyList())
    val cachedQuestions: StateFlow<List<Question>> = _cachedQuestions

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _cachedQuestions.value = cacheRepository.getCachedQuestions()
        }
    }
}
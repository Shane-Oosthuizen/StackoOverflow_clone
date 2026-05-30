package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.OrderEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Search
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SearchTypeEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SortEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.repository.SearchRepository
import com.shaneoosthuizen.assessment.clonedstackoverflow.dependencies.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val prefs: SharedPreferencesManager
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _searchSortOption = MutableStateFlow(prefs.searchSort)
    val searchSortOption: StateFlow<SortEnum> = _searchSortOption

    private val _searchOrderOption = MutableStateFlow(prefs.searchOrder)
    val searchOrderOption: StateFlow<OrderEnum> = _searchOrderOption

    private val _searchType = MutableStateFlow(prefs.searchType)

    val searchType: StateFlow<SearchTypeEnum> = _searchType

    private val _sortQuestionOption = MutableStateFlow(prefs.questionSort)
    val sortQuestionOption: StateFlow<SortEnum> = _sortQuestionOption

    private val _orderQuestionOption = MutableStateFlow(prefs.questionOrder)
    val orderQuestionOption: StateFlow<OrderEnum> = _orderQuestionOption

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getQuestions()
    }

    fun getQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                repository.getQuestions().collect { questions ->
                    _questions.value = questions
                    sortQuestions()
                }
            } catch (e: Exception) {
                android.util.Log.e("SearchViewModel", "getQuestions FAILED: ${e.message}", e)
                // temporarily rethrow to see full crash details:
                throw e
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchText(newText: String) {
        _searchText.value = newText
    }

    fun updateSearchSortOption(sort: SortEnum) {
        _searchSortOption.value = sort
        prefs.searchSort = sort
    }

    fun updateSearchOrderOption(order: OrderEnum) {
        _searchOrderOption.value = order
        prefs.searchOrder = order
    }

    fun updateSearchType(type: SearchTypeEnum) {
        _searchType.value = type
        prefs.searchType = type
    }

    fun performSearch() {
        if (_searchText.value.isBlank()) return  // do nothing, don't touch questions list

        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val search = Search(
                    SearchText = _searchText.value,
                    PageSize = 20,
                    Order = _searchOrderOption.value,
                    Sort = _searchSortOption.value,
                    SearchType = _searchType.value
                )
                repository.searchTopic(search).collect { results ->
                    _questions.value = results
                }
            } catch (e: Exception) {
                android.util.Log.e("SearchViewModel", "Search failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateQuestionSortOption(sort: SortEnum) {
        _sortQuestionOption.value = sort
        prefs.questionSort = sort
        sortQuestions()
    }

    fun updateQuestionOrderOption(order: OrderEnum) {
        _orderQuestionOption.value = order
        prefs.questionOrder = order
        sortQuestions()
    }

    fun sortQuestions() {
        val sorted = when (_sortQuestionOption.value) {
            SortEnum.ACTIVITY -> _questions.value.sortedBy { it.lastActivityDate }
            SortEnum.VOTES -> _questions.value.sortedBy { it.score }
            SortEnum.CREATION -> _questions.value.sortedBy { it.creationDate }
            SortEnum.RELEVANCE -> _questions.value.toList()
        }

        _questions.value = if (_orderQuestionOption.value == OrderEnum.ASCENDING) {
            sorted.toList()
        } else {
            sorted.reversed()
        }
    }
}
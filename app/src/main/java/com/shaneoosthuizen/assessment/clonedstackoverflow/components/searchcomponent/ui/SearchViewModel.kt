package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.OrderEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Question
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Search
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SearchTypeEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SortEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.repository.SearchRepository
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.networkmonitor.NetworkMonitor
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
    private val connection: NetworkMonitor,
    private val prefs: SharedPreferencesManager
) : ViewModel() {
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _questionAmount = MutableStateFlow(prefs.questionListAmount)
    val questionAmount: StateFlow<Int> = _questionAmount

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
            connectionWatcher()
        }

    fun connectionWatcher() {
        viewModelScope.launch(Dispatchers.IO) {
            connection.isConnected.collect { isConnected ->
                if (isConnected) {
                    _isConnected.value = true
                    getQuestions()
                }
            }
        }
    }
    fun getQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                repository.getQuestions(_questionAmount.value).collect { questions ->
                    _questions.value = questions
                    sortQuestions()
                }
            } catch (e: Exception) {
                android.util.Log.e("SearchViewModel", "getQuestions FAILED: ${e.message}", e)
                throw e
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchText(newText: String) {
        _searchText.value = newText
    }

    fun updatePageSize(size: Int) {
        _questionAmount.value = size
        prefs.questionSearchAmount = size
    }

    fun updateQuestionList(size: Int){
        _questionAmount.value = size
         prefs.questionListAmount = size
         getQuestions()
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
        if (_searchText.value.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val search = Search(
                    SearchText = _searchText.value,
                    PageSize = _questionAmount.value,
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

    fun searchTag(tag: String) {
        if (tag.isBlank()) return
        _searchText.value = "[ $tag ]"
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                repository.searchTag(tag).collect { results ->
                    _questions.value = results
                }
            } catch (e: Exception) {
                android.util.Log.e("SearchViewModel", "Search by tag failed: ${e.message}")
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
        }

        _questions.value = if (_orderQuestionOption.value == OrderEnum.ASCENDING) {
            sorted.toList()
        } else {
            sorted.reversed()
        }
    }
}
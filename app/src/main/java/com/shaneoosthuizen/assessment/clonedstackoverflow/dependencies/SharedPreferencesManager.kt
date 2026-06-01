package com.shaneoosthuizen.assessment.clonedstackoverflow.dependencies

import android.content.SharedPreferences
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.OrderEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SearchTypeEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SortEnum
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(
    private val prefs: SharedPreferences
) {
    companion object {
        private const val KEY_SEARCH_SORT = "search_sort"
        private const val KEY_SEARCH_ORDER = "search_order"
    }

    var searchSort: SortEnum
        get() {
            val value = prefs.getString(KEY_SEARCH_SORT, SortEnum.ACTIVITY.value)
            return SortEnum.entries.firstOrNull { it.value == value } ?: SortEnum.ACTIVITY
        }
        set(sort) = prefs.edit().putString(KEY_SEARCH_SORT, sort.value).apply()

    var searchOrder: OrderEnum
        get() {
            val value = prefs.getString(KEY_SEARCH_ORDER, OrderEnum.DESCENDING.value)
            return OrderEnum.entries.firstOrNull { it.value == value } ?: OrderEnum.DESCENDING
        }
        set(order) = prefs.edit().putString(KEY_SEARCH_ORDER, order.value).apply()

    var searchType: SearchTypeEnum
        get() {
            val value = prefs.getString("search_type", SearchTypeEnum.PHRASE.value)
            return SearchTypeEnum.entries.firstOrNull { it.value == value } ?: SearchTypeEnum.PHRASE
        }
        set(type) = prefs.edit().putString("search_type", type.value).apply()

    var questionSearchAmount: Int
        get() = prefs.getInt("search_page_size", 20)
        set(size) = prefs.edit().putInt("search_page_size", size).apply()

    var questionListAmount: Int
        get() = prefs.getInt("question_list_page_size", 20)
        set(size) = prefs.edit().putInt("question_list_page_size", size).apply()

    var questionSort: SortEnum
        get() {
            val value = prefs.getString("question_sort", SortEnum.VOTES.value)
            return SortEnum.entries.firstOrNull { it.value == value } ?: SortEnum.VOTES
        }
        set(sort) = prefs.edit().putString("question_sort", sort.value).apply()

        var questionOrder: OrderEnum
        get() {
            val value = prefs.getString("question_order", OrderEnum.DESCENDING.value)
            return OrderEnum.entries.firstOrNull { it.value == value } ?: OrderEnum.DESCENDING
        }
        set(order) = prefs.edit().putString("question_order", order.value).apply()

}

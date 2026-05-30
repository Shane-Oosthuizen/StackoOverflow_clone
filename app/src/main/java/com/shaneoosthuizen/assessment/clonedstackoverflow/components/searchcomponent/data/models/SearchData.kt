package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.data.models

import com.google.gson.annotations.SerializedName
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.OrderEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Search
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SearchTypeEnum
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.SortEnum

data class SearchData(
    @SerializedName("search_text") val SearchText: String,
    @SerializedName("page_size")val PageSize: Int,
    @SerializedName("order")val Order: String,
    @SerializedName("sort")val Sort: String,
    @SerializedName("search_type")val SearchType: String
)

fun SearchData.toDomain(): Search {
    return Search(
        SearchText = SearchText,
        PageSize = PageSize,
        Order = OrderEnum.valueOf(Order.uppercase()),
        Sort = SortEnum.valueOf(Sort.uppercase()),
        SearchType = SearchTypeEnum.valueOf(SearchType.uppercase())
    )
}

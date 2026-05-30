package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models

class Search(
    val SearchText: String,
    val PageSize: Int,
    val Order: OrderEnum,
    val Sort: SortEnum,
    val SearchType: SearchTypeEnum
)
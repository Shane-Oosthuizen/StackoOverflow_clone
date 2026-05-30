package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models

enum class SortEnum(val value : String) {
    ACTIVITY("activity"),
    CREATION("creation"),
    VOTES("votes"),
    RELEVANCE("relevance");

    override fun toString(): String {
        return value
    }
}
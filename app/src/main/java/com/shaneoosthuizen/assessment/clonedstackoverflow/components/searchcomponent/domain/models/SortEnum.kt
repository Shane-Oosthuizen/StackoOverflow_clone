package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models

enum class SortEnum(val value: String, val displayName: String) {
    ACTIVITY("activity", "Activity"),
    CREATION("creation", "Creation"),
    VOTES("votes", "Votes"),
    RELEVANCE("relevance", "Relevance");

    override fun toString(): String {
        return value
    }
}
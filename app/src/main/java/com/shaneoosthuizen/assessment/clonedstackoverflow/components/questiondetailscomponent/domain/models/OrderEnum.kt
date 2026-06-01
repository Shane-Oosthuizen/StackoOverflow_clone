package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models

enum class OrderEnum(val value: String, val displayName: String) {
    ASCENDING("asc", "Ascending"),
    DESCENDING("desc", "Descending");

    override fun toString(): String {
        return value
    }
}
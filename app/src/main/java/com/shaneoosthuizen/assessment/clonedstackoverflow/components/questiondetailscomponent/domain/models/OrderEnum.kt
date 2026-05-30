package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models

enum class OrderEnum(val value: String){
    ASCENDING("asc"),
    DESCENDING("desc");

    override fun toString(): String {
        return value
    }
}
package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models

enum class QuestionTypeEnum(val value: String, val displayText: String) {
    TROUBLESHOOTING("troubleshooting", "Troubleshooting"),
    ADVICE("advice", "Advice"),
    BEST_PRACTICES("best_Practices", "Best Practices"),
    TOOLING("tooling", "Tooling"),
    OTHER("other", "Other");

    override fun toString(): String {
        return value
    }
}
package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models

data class Question(
    val questionId: Int,
    val title: String,
    val link: String,
    val score: Int,
    val author: String,
    val profileImage: String?,
    val tags: List<String>,
    val isAnswered: Boolean,
    val viewCount: Int,
    val answerCount: Int,
    val acceptedAnswerId: Int?,
    val creationDate: Long,
    val lastActivityDate: Long,
    val lastEditDate: Long?,
    val contentLicense: String
)
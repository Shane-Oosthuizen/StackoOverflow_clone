package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models

data class Answer(
    val answerId: Int,
    val questionId: Int,
    val score: Int,
    val isAccepted: Boolean,
    val author: String,
    val profileImage: String?,
    val creationDate: Long,
    val lastActivityDate: Long,
    val lastEditDate: Long?,
    val contentLicense: String,
    val body: String,
    val commentCount: Int
)

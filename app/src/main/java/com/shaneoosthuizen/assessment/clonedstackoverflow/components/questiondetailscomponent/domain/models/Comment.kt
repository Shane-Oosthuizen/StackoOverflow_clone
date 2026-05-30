package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models

data class Comment(
    val commentId: Int,
    val postId: Int,
    val score: Int,
    val author: String,
    val profileImage: String?,
    val creationDate: Long,
    val body: String
)


package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.answer.models

import com.google.gson.annotations.SerializedName
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Comment

data class CommentData(
    @SerializedName("comment_id") val commentId: Int,
    @SerializedName("post_id") val postId: Int,
    @SerializedName("score") val score: Int,
    @SerializedName("owner") val owner: AnswerOwnerData?,
    @SerializedName("creation_date") val creationDate: Long,
    @SerializedName("body") val body: String?
)

data class CommentsResponse(@SerializedName("items") val items: List<CommentData>)

fun CommentData.toDomain(): Comment = Comment(
    commentId = commentId,
    postId = postId,
    score = score,
    author = owner?.displayName ?: "Unknown",
    profileImage = owner?.profileImage,
    creationDate = creationDate,
    body = body ?: ""
)


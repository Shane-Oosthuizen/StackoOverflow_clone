package com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.answer.models

import com.google.gson.annotations.SerializedName
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Answer

data class AnswerData(
    @SerializedName("answer_id") val answerId: Int,
    @SerializedName("question_id") val questionId: Int,
    @SerializedName("score") val score: Int,
    @SerializedName("is_accepted") val isAccepted: Boolean,
    @SerializedName("owner") val owner: AnswerOwnerData?,
    @SerializedName("creation_date") val creationDate: Long,
    @SerializedName("last_activity_date") val lastActivityDate: Long,
    @SerializedName("last_edit_date") val lastEditDate: Long?,
    @SerializedName("content_license") val contentLicense: String?,
    @SerializedName("body") val body: String?,
    @SerializedName("comment_count") val commentCount: Int?
)

data class AnswerOwnerData(
    @SerializedName("display_name") val displayName: String,
    @SerializedName("profile_image") val profileImage: String?
)

data class AnswersResponse(@SerializedName("items") val items: List<AnswerData>)

fun AnswerData.toDomain(): Answer = Answer(
    answerId = answerId,
    questionId = questionId,
    score = score,
    isAccepted = isAccepted,
    author = owner?.displayName ?: "Unknown",
    profileImage = owner?.profileImage,
    creationDate = creationDate,
    lastActivityDate = lastActivityDate,
    lastEditDate = lastEditDate,
    contentLicense = contentLicense ?: "",
    body = body ?: "",
    commentCount = commentCount ?: 0
)



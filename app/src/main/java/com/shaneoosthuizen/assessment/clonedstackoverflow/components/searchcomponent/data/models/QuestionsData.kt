package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.data.models

import com.google.gson.annotations.SerializedName
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models.Question

data class QuestionData(
    @SerializedName("question_id") val questionId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("link") val link: String,
    @SerializedName("score") val score: Int,
    @SerializedName("owner") val owner: OwnerData?,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("is_answered") val isAnswered: Boolean,
    @SerializedName("view_count") val viewCount: Int,
    @SerializedName("answer_count") val answerCount: Int,
    @SerializedName("accepted_answer_id") val acceptedAnswerId: Int?,
    @SerializedName("creation_date") val creationDate: Long,
    @SerializedName("last_activity_date") val lastActivityDate: Long,
    @SerializedName("last_edit_date") val lastEditDate: Long?,
    @SerializedName("content_license") val contentLicense: String?
)

data class OwnerData(
    @SerializedName("display_name") val displayName: String,
    @SerializedName("profile_image") val profileImage: String?
)

data class QuestionsResponse(@SerializedName("items") val items: List<QuestionData>)

fun QuestionData.toDomain(): Question {
    return Question(
        questionId = questionId,
        title = title,
        link = link,
        score = score,
        author = owner?.displayName ?: "Unknown",
        profileImage = owner?.profileImage,
        tags = tags,
        isAnswered = isAnswered,
        viewCount = viewCount,
        answerCount = answerCount,
        acceptedAnswerId = acceptedAnswerId,
        creationDate = creationDate,
        lastActivityDate = lastActivityDate,
        lastEditDate = lastEditDate,
        contentLicense = contentLicense ?: ""
    )
}
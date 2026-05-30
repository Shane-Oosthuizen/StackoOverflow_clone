package com.shaneoosthuizen.assessment.clonedstackoverflow.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val questionId: Int,
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
    val contentLicense: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "answers")
data class AnswerEntity(
    @PrimaryKey val answerId: Int,
    val questionId: Int,
    val isAccepted: Boolean,
    val score: Int,
    val author: String,
    val profileImage: String?,
    val creationDate: Long,
    val lastActivityDate: Long,
    val lastEditDate: Long?,
    val contentLicense: String,
    val body: String,
    val commentCount: Int
)

@Entity(tableName = "comment")
data class CommentEntity(
    @PrimaryKey val commentId: Int,
    val postId: Int,
    val score: Int,
    val author: String,
    val profileImage: String?,
    val creationDate: Long,
    val body: String
)


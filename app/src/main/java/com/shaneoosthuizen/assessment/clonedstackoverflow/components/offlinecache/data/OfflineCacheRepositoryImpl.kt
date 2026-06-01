package com.shaneoosthuizen.assessment.clonedstackoverflow.components.offlinecache.data

import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.AnswerEntity
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.QuestionEntity
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.dao.AnswerDao
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.dao.QuestionDao
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.offlinecache.domain.OfflineCacheRepository
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Answer
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.domain.models.Question

import javax.inject.Inject

private const val CACHE_LIMIT = 10

class OfflineCacheRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao
) : OfflineCacheRepository {

    override suspend fun cacheQuestion(question: Question, answers: List<Answer>) {
        val alreadyCached = questionDao.getQuestionById(question.questionId) != null
        if (!alreadyCached && questionDao.getCachedCount() >= CACHE_LIMIT) {
            questionDao.getOldestCachedQuestion()?.let { oldest ->
                answerDao.deleteAnswersForQuestion(oldest.questionId)
                questionDao.deleteQuestionById(oldest.questionId)
            }
        }
        questionDao.insertQuestion(question.toEntity())
        answerDao.deleteAnswersForQuestion(question.questionId)
        answerDao.insertAnswers(answers.map { it.toEntity() })
    }

    override suspend fun getCachedQuestions(): List<Question> =
        questionDao.getAllCachedQuestions().map { it.toDomain() }

    override suspend fun getCachedQuestion(questionId: Int): Question? {
        val entity = questionDao.getQuestionById(questionId) ?: return null
        val answers = answerDao.getAnswersForQuestion(questionId).map { it.toDomain() }
        return entity.toDomain(answers)
    }
}

private fun Question.toEntity() = QuestionEntity(
    questionId = questionId, title = title, link = link, score = score,
    author = author, profileImage = profileImage, tags = tags,
    isAnswered = isAnswered, viewCount = viewCount, answerCount = answerCount,
    acceptedAnswerId = acceptedAnswerId, creationDate = creationDate,
    lastActivityDate = lastActivityDate, lastEditDate = lastEditDate,
    contentLicense = contentLicense, cachedAt = System.currentTimeMillis()
)

private fun QuestionEntity.toDomain(answers: List<Answer> = emptyList()) = Question(
    questionId = questionId, title = title, link = link, score = score,
    author = author, profileImage = profileImage, tags = tags,
    isAnswered = isAnswered, viewCount = viewCount, answerCount = answerCount,
    acceptedAnswerId = acceptedAnswerId, creationDate = creationDate,
    lastActivityDate = lastActivityDate, lastEditDate = lastEditDate,
    contentLicense = contentLicense, answers = answers
)

private fun Answer.toEntity() = AnswerEntity(
    answerId = answerId, questionId = questionId, isAccepted = isAccepted,
    score = score, author = author, profileImage = profileImage,
    creationDate = creationDate, lastActivityDate = lastActivityDate,
    lastEditDate = lastEditDate, contentLicense = contentLicense,
    body = body, commentCount = commentCount
)

private fun AnswerEntity.toDomain() = Answer(
    answerId = answerId, questionId = questionId, isAccepted = isAccepted,
    score = score, author = author, profileImage = profileImage,
    creationDate = creationDate, lastActivityDate = lastActivityDate,
    lastEditDate = lastEditDate, contentLicense = contentLicense,
    body = body, commentCount = commentCount
)
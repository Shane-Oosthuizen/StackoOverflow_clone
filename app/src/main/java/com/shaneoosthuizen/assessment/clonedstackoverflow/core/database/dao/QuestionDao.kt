package com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.QuestionEntity

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions ORDER BY cachedAt DESC")
    suspend fun getAllCachedQuestions(): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE questionId = :questionId")
    suspend fun getQuestionById(questionId: Int): QuestionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Query("DELETE FROM questions WHERE questionId = :questionId")
    suspend fun deleteQuestionById(questionId: Int)

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getCachedCount(): Int

    @Query("SELECT * FROM questions ORDER BY cachedAt ASC LIMIT 1")
    suspend fun getOldestCachedQuestion(): QuestionEntity?
}
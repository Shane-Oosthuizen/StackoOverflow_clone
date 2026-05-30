package com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.AnswerEntity

@Dao
interface AnswerDao {

    @Query("SELECT * FROM answers WHERE questionId = :questionId")
    suspend fun getAnswersForQuestion(questionId: Int): List<AnswerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(answers: List<AnswerEntity>)

    @Query("DELETE FROM answers WHERE questionId = :questionId")
    suspend fun deleteAnswersForQuestion(questionId: Int)
}
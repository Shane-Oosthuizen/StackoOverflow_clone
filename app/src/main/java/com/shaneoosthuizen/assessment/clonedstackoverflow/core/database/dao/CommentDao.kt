package com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.CommentEntity

@Dao
interface CommentDao {

    @Query("SELECT * FROM comment WHERE postId = :postId")
    suspend fun getCommentsForPost(postId: Int): List<CommentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Query("DELETE FROM comment WHERE postId = :postId")
    suspend fun deleteCommentsForPost(postId: Int)
}
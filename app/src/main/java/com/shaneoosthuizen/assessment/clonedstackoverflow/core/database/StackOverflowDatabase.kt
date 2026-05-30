package com.shaneoosthuizen.assessment.clonedstackoverflow.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.dao.AnswerDao
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.dao.CommentDao
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.dao.QuestionDao

@Database(
    entities = [
        QuestionEntity::class,
        AnswerEntity::class,
        CommentEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StackOverflowDatabase: RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun answerDao(): AnswerDao
    abstract fun commentDao(): CommentDao
}
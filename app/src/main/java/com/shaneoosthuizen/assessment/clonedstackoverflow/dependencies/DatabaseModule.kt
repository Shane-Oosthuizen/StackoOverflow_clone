package com.shaneoosthuizen.assessment.clonedstackoverflow.dependencies

import android.content.Context
import androidx.room.Room
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.StackOverflowDatabase
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.dao.AnswerDao
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.dao.CommentDao
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.database.dao.QuestionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StackOverflowDatabase =
        Room.databaseBuilder(context, StackOverflowDatabase::class.java, "stackoverflow_cache")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideQuestionDao(db: StackOverflowDatabase): QuestionDao = db.questionDao()

    @Provides
    fun provideAnswerDao(db: StackOverflowDatabase): AnswerDao = db.answerDao()

    @Provides
    fun provideCommentDao(db: StackOverflowDatabase): CommentDao = db.commentDao()
}
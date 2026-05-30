package com.shaneoosthuizen.assessment.clonedstackoverflow.dependencies

import com.shaneoosthuizen.assessment.clonedstackoverflow.BuildConfig
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.answer.AnswerAPIService
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.questiondetailscomponent.data.question.QuestionAPIService
import com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.data.SearchApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    @Singleton
    @Named("BASE_URL")
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(@Named("BASE_URL") baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideSearchApiService(retrofit: Retrofit): SearchApiService =
        retrofit.create(SearchApiService::class.java)

    @Provides
    @Singleton
    fun provideQuestionApiService(retrofit: Retrofit): QuestionAPIService =
        retrofit.create(QuestionAPIService::class.java)

    @Provides
    @Singleton
    fun provideAnswerApiService(retrofit: Retrofit): AnswerAPIService =
        retrofit.create(AnswerAPIService::class.java)
}
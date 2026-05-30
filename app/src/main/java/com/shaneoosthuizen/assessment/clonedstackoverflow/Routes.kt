package com.shaneoosthuizen.assessment.clonedstackoverflow

import java.net.URLEncoder

object Routes {
    const val SEARCH_SCREEN = "search_screen"
    const val QUESTION_SCREEN = "question_screen/{questionId}"
    const val ANSWER_SCREEN = "answer_screen/{answerId}"
    const val OFFLINE_SCREEN = "offline_screen"

    fun questionSelectedScreen(questionId: Int): String = "question_screen/$questionId"
    fun answerSelectedScreen(answerId: Int): String = "answer_screen/$answerId"
}
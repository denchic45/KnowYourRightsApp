package com.denchic45.knowyourrights.domain.model

import com.denchic45.knowyourrights.ui.model.QuizItem
import com.denchic45.knowyourrights.utils.UUIDS
import java.time.LocalDateTime
import java.util.*

data class QuizResult(
    override val id: String,
    val quizItem: QuizItem,
    val passedQuestions: List<PassedQuestion>,
    val timestamp: LocalDateTime
) : DomainModel {
    fun countOfCorrects(): Int {
        return passedQuestions.count { it.isCorrectAnswer }
    }
}

data class PassedQuestion(
    val question: Question,
    val answer: Question.Answer,
    val id: String = UUIDS.createShort()
) {
    val isCorrectAnswer: Boolean
        get() = question.tryAnswer(answer)
}
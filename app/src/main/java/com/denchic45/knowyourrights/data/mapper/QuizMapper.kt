package com.denchic45.knowyourrights.data.mapper

import com.denchic45.knowyourrights.data.model.QuizEntity
import com.denchic45.knowyourrights.data.model.QuizResultEntity
import com.denchic45.knowyourrights.domain.model.QuizResult
import com.denchic45.knowyourrights.ui.model.QuizItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(uses = [AnswerMapper::class])
abstract class QuizMapper {

    abstract fun entityToDomain(quizEntity: QuizEntity, questionsCount:Int, maxResult: Int): QuizItem

    @Mapping(source = "quizItem.id", target = "quizId")
    abstract fun domainToEntity(quizResult: QuizResult): QuizResultEntity

}
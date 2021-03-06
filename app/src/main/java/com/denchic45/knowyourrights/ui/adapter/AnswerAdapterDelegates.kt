package com.denchic45.knowyourrights.ui.adapter

import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import com.denchic45.knowyourrights.databinding.ItemAnswerBinding
import com.denchic45.knowyourrights.domain.model.PassedQuestion
import com.denchic45.knowyourrights.domain.model.Question
import com.denchic45.knowyourrights.ui.model.EnterChoiceItem
import com.denchic45.knowyourrights.ui.model.MultiChoiceItem
import com.denchic45.knowyourrights.ui.model.SingleChoiceItem
import com.denchic45.knowyourrights.utils.viewBinding
import com.denchic45.widget.extendedAdapter.DelegationAdapterExtended
import com.denchic45.widget.extendedAdapter.ListItemAdapterDelegate
import com.denchic45.widget.extendedAdapter.adapter

class AnswerAdapterDelegate :
    ListItemAdapterDelegate<PassedQuestion, AnswerAdapterDelegate.AnswerHolder>() {

    class AnswerHolder(itemAnswerBinding: ItemAnswerBinding) :
        BaseViewHolder<PassedQuestion, ItemAnswerBinding>(itemAnswerBinding) {
        override fun onBind(item: PassedQuestion) {
            with(binding) {
                tvQuestionName.text = item.question.title
                val adapter: DelegationAdapterExtended = adapter {
                    delegates(
                        SingleChoiceAdapterDelegate(true),
                        MultiChoiceAdapterDelegate(true),
                        EnterAnswerAdapterDelegate(true)
                    )
                }
                rvAnswers.adapter = adapter

                when (item.answer) {
                    is Question.Answer.SingleAnswer -> {
                        adapter.submit((item.question.choice as Question.Choice.SingleChoice).answers
                            .map { someAnswer ->
                                SingleChoiceItem(
                                    answer = someAnswer,
                                    isChecked = item.answer.value == someAnswer,
                                    isCorrect = when {
                                        item.answer.value == someAnswer -> {
                                            item.isCorrectAnswer
                                        }
                                        item.question.choice.correctAnswer.value == someAnswer -> true
                                        else -> null
                                    }
                                )
                            })
//                        Handler(Looper.getMainLooper()).post {
//                            adapter.notifyItemChanged(
//                                item.question.choice.answers.indexOf(
//                                    item.question.choice.correctAnswer.value
//                                ), SingleChoiceAdapterDelegate.PAYLOAD_TRUE
//                            )
//                            if (item.question.choice.correctAnswer.value != item.answer.value) {
//                                adapter.notifyItemChanged(
//                                    item.question.choice.answers.indexOf(
//                                        item.answer.value
//                                    ), SingleChoiceAdapterDelegate.PAYLOAD_FALSE
//                                )
//                            }
//                        }
                    }
                    is Question.Answer.MultiAnswer -> {
                        val passedAnswers = (item.question.choice as Question.Choice.MultiChoice)
                            .getCorrectAndWrongAnswerPositions(
                                item.answer
                            )
                        adapter.submit(
                            item.question.choice.answers
                                .mapIndexed { index, someAnswer ->
                                    MultiChoiceItem(
                                        answer = someAnswer,
                                        isChecked = item.answer.value.contains(someAnswer),
                                        isCorrect = when {
                                            passedAnswers.first.contains(index) -> true
                                            passedAnswers.second.contains(index) -> false
                                            else -> null
                                        }
                                    )
                                })

//                        Handler(Looper.getMainLooper()).post {
//                            item.question.choice.getCorrectAndWrongAnswerPositions(
//                                item.answer
//                            ).apply {
//                                first.forEach {
//                                    adapter.notifyItemChanged(
//                                        it,
//                                        MultiChoiceAdapterDelegate.PAYLOAD_TRUE
//                                    )
//                                }
//                                second.forEach {
//                                    adapter.notifyItemChanged(
//                                        it,
//                                        MultiChoiceAdapterDelegate.PAYLOAD_FALSE
//                                    )
//                                }
//                            }
//                        }
                    }
                    is Question.Answer.EnterAnswer -> {
                        adapter.submit(
                            listOf(
                                EnterChoiceItem(
                                    enteredAnswer = item.answer.value
                                )
                            )
                        )

                        Handler(Looper.getMainLooper()).post {
                            adapter.notifyItemChanged(
                                0,
                                if ((item.question.choice.correctAnswer as Question.Answer.EnterAnswer).value != item.answer.value) {
                                    EnterAnswerAdapterDelegate.Companion.Payload.False((item.question.choice.correctAnswer as Question.Answer.EnterAnswer).value)
                                } else {
                                    EnterAnswerAdapterDelegate.Companion.Payload.True
                                }
                            )
                        }
                    }
                }


            }
        }

    }

    override fun isForViewType(item: Any): Boolean {
        return item is PassedQuestion
    }

    override fun onBindViewHolder(item: PassedQuestion, holder: AnswerHolder) {
        holder.onBind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup): AnswerHolder {
        return AnswerHolder(parent.viewBinding(ItemAnswerBinding::inflate))
    }
}
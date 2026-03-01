package com.blacksun.controller

import com.blacksun.dto.TelegramUpdate
import com.blacksun.service.SurveyService
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.http.annotation.*

@Controller("/webhook")
class TelegramController(
    private val surveyService: SurveyService
) {

    @Post
    @ExecuteOn(TaskExecutors.BLOCKING)
    fun webhook(@Body update: TelegramUpdate): String {

        val message = update.message ?: return "ok"
        val text = message.text ?: return "ok"

        surveyService.handleMessage(
            message.chat.id,
            text
        )

        return "ok"
    }
}
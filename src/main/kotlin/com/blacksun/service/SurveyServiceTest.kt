package com.blacksun.service

import com.blacksun.client.TelegramClient
import jakarta.inject.Singleton

@Singleton
class SurveyService(
    private val telegramClient: TelegramClient
) {

    fun handleMessage(chatId: Long, text: String) {
        when (text.lowercase()) {

            "/start" -> telegramClient.sendMessage(
                chatId,
                "👋 Добро пожаловать!\n\nПожалуйста, отправьте номер телефона."
            )

            else -> telegramClient.sendMessage(
                chatId,
                "Я пока понимаю только команду /start"
            )
        }
    }
}
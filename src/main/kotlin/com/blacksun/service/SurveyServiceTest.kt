package com.blacksun.service

import com.blacksun.client.TelegramClient
import jakarta.inject.Singleton

data class UserSurvey(
    var phone: String? = null,
    var projectName: String? = null,
    var description: String? = null,
    var captain: String? = null
)

enum class UserState {
    WAITING_FOR_PHONE,
    WAITING_FOR_PROJECT_NAME,
    WAITING_FOR_DESCRIPTION,
    WAITING_FOR_CAPTAIN,
    COMPLETED
}

@Singleton
class SurveyService(
    private val telegramClient: TelegramClient
) {
    private val surveys = mutableMapOf<Long, UserSurvey>()
    private val userStates = mutableMapOf<Long, UserState>()

    private val phoneRegex = Regex("^(\\+7|8)\\d{10}$") // +7XXXXXXXXXX или 8XXXXXXXXXX

    private val projectDescriptionRegex = Regex("^медицина|программирование|география|бизнес")

    fun handleMessage(chatId: Long, text: String) {
        val state = userStates[chatId]
        when {

            text.lowercase() == "/start" -> {
                userStates[chatId] = UserState.WAITING_FOR_PHONE
                surveys[chatId] = UserSurvey()
                telegramClient.sendMessage(
                    chatId,
                    "👋 Добро пожаловать!\n\nПожалуйста, отправьте номер телефона."
                )
            }

            text.lowercase() == "/retp" -> {
                userStates[chatId] = UserState.WAITING_FOR_PHONE
                surveys[chatId] = UserSurvey()
                telegramClient.sendMessage(
                    chatId,
                    "Отправьте новый номер телефона."
                )
            }

            text.lowercase() == "/help" -> telegramClient.sendMessage(
                chatId,
                """
            📌 Доступные команды:
            /start – начать
            /help – помощь
            """.trimIndent()
            )
            state == UserState.WAITING_FOR_PHONE -> {
                if (phoneRegex.matches(text)) {

                    surveys[chatId]?.phone = text
                    userStates[chatId] = UserState.WAITING_FOR_PROJECT_NAME

                    telegramClient.sendMessage(
                        chatId,
                        "✅ Телефон сохранён.\n\nВведите название проекта."
                    )
                } else {
                    telegramClient.sendMessage(
                        chatId,
                        "❌ Неверный формат телефона."
                    )
                }
            }
            state == UserState.WAITING_FOR_PROJECT_NAME -> {

                surveys[chatId]?.projectName = text
                userStates[chatId] = UserState.WAITING_FOR_DESCRIPTION

                telegramClient.sendMessage(
                    chatId,
                    """✅ Спасибо! Название сохранёно. 
Выберите направление вашего проекта. Доступные направления:
Медицина, Программирование, География, Бизнес""".trimIndent()
                )
            }

            state == UserState.WAITING_FOR_DESCRIPTION -> {

                if (projectDescriptionRegex.matches(text.lowercase())) {
                    surveys[chatId]?.description = text
                    userStates[chatId] = UserState.WAITING_FOR_CAPTAIN
                    telegramClient.sendMessage(
                        chatId,
                        "✅ Спасибо! Направление выбрано. Теперь введите имя своего капитана (без пробелов)."
                    )
                } else {
                    telegramClient.sendMessage(
                        chatId,
                        "❌ Такого направления не существует. Введите снова."
                    )
                }
            }
            state == UserState.WAITING_FOR_CAPTAIN -> {
                if (!text.contains(" ")) {
                    surveys[chatId]?.captain = text
                    userStates[chatId] = UserState.COMPLETED

                    telegramClient.sendMessage(
                        chatId,
                        "✅ Спасибо! Все данные сохранены!"
                    )
                    telegramClient.sendMessage(
                        chatId,
                        """Данные получены.
Номер телефона: ${surveys[chatId]?.phone}
Название проекта: ${surveys[chatId]?.projectName}
Направление: ${surveys[chatId]?.description}
Имя капитана: ${surveys[chatId]?.captain}
                            
                        """.trimIndent()
                    )
                }
                else {
                    telegramClient.sendMessage(
                        chatId,
                        """❌ Введите имя капитана без пробелов.""".trimIndent()
                    )
                }
            }
        }
    }
}

// номер телефона (сохранить)
// название проекта + сохранение
// имя капитана + сохранение
// назначение + сохранение

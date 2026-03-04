package com.blacksun.service

import com.blacksun.client.TelegramClient
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SurveyServiceTest {

    private lateinit var telegramClient: TelegramClient
    private lateinit var service: SurveyService

    @BeforeEach
    fun setup() {
        telegramClient = mockk(relaxed = true)
        service = SurveyService(telegramClient)
    }

    // 1. /start должен просить номер телефона
    @Test
    fun `start command should request phone`() {
        service.handleMessage(1L, "/start")

        verify {
            telegramClient.sendMessage(
                1L,
                match { it.contains("номер телефона") }
            )
        }
    }

    // 2. Сообщение без start
    @Test
    fun `message before start should ask to start`() {
        service.handleMessage(2L, "hello")

        verify {
            telegramClient.sendMessage(
                2L,
                "Напишите /start чтобы начать."
            )
        }
    }

    // 3. Верный телефон
    @Test
    fun `valid phone after start should be accepted`() {
        service.handleMessage(3L, "/start")
        service.handleMessage(3L, "+79991234567")

        verify {
            telegramClient.sendMessage(
                3L,
                match { it.contains("Телефон сохранён") }
            )
        }
    }

    // 4. Неверный телефон
    @Test
    fun `invalid phone should show error`() {
        service.handleMessage(4L, "/start")
        service.handleMessage(4L, "12345")

        verify {
            telegramClient.sendMessage(
                4L,
                match { it.contains("Неверный формат") }
            )
        }
    }

    // 5. Название проекта
    @Test
    fun `project name should be saved`() {
        service.handleMessage(5L, "/start")
        service.handleMessage(5L, "+79991234567")
        service.handleMessage(5L, "MyProject")

        verify {
            telegramClient.sendMessage(
                5L,
                match { it.contains("направление") || it.contains("Выберите") }
            )
        }
    }

    // 6. Неверное направление
    @Test
    fun `invalid direction should be rejected`() {
        service.handleMessage(6L, "/start")
        service.handleMessage(6L, "+79991234567")
        service.handleMessage(6L, "ProjectX")
        service.handleMessage(6L, "Игры")

        verify {
            telegramClient.sendMessage(
                6L,
                match { it.contains("не существует") }
            )
        }
    }

    // 7. Верное направление
    @Test
    fun `valid direction should be accepted`() {
        service.handleMessage(7L, "/start")
        service.handleMessage(7L, "+79991234567")
        service.handleMessage(7L, "ProjectX")
        service.handleMessage(7L, "медицина")

        verify {
            telegramClient.sendMessage(
                7L,
                match { it.contains("капитана") }
            )
        }
    }

    // 8. Капитан без пробелов
    @Test
    fun `captain name without spaces should be accepted`() {
        service.handleMessage(8L, "/start")
        service.handleMessage(8L, "+79991234567")
        service.handleMessage(8L, "ProjectX")
        service.handleMessage(8L, "бизнес")
        service.handleMessage(8L, "CaptainName")

        verify {
            telegramClient.sendMessage(
                8L,
                match { it.contains("Все данные сохранены") }
            )
        }
    }

    // 9. Капитан с пробелом
    @Test
    fun `captain name with space should be rejected`() {
        service.handleMessage(9L, "/start")
        service.handleMessage(9L, "+79991234567")
        service.handleMessage(9L, "ProjectX")
        service.handleMessage(9L, "бизнес")
        service.handleMessage(9L, "Captain Name")

        verify {
            telegramClient.sendMessage(
                9L,
                match { it.contains("без пробелов") })
        }
    }

    // 10. Полный опрос
    @Test
    fun `full survey flow should complete`() {
        service.handleMessage(10L, "/start")
        service.handleMessage(10L, "+79991234567")
        service.handleMessage(10L, "ProjectX")
        service.handleMessage(10L, "медицина")
        service.handleMessage(10L, "Captain")

        verify {
            telegramClient.sendMessage(
                10L,
                match { it.contains("Все данные сохранены") }
            )
        }
    }
}
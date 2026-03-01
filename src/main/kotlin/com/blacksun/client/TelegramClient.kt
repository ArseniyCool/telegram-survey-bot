package com.blacksun.client

import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import jakarta.inject.Singleton

@Singleton
class TelegramClient(
    @Value("\${telegram.token}") private val token: String,

    @Client("https://api.telegram.org") private val httpClient: HttpClient
) {

    fun sendMessage(chatId: Long, text: String) {

        val url = "/bot$token/sendMessage"

        val payload = mapOf(
            "chat_id" to chatId,
            "text" to text
        )

        httpClient.toBlocking().exchange(
            HttpRequest.POST(url, payload),
            String::class.java
        )
    }
}
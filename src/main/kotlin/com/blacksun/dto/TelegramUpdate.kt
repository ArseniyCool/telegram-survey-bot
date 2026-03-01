package com.blacksun.dto

data class TelegramUpdate(
    val message: Message?
)

data class Message(
    val chat: Chat = Chat(0),
    val text: String? = null
)

data class Chat(
    val id: Long = 0
)


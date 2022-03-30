package me.cniekirk.cryptotracker.data.model

sealed class SocketEvent {
    data class Error(val error: Throwable): SocketEvent()
    data class CloseEvent(val code: Int, val reason: String): SocketEvent()
    data class StringMessage(val content: String): SocketEvent()
}

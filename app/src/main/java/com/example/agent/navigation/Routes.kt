package com.example.agent.navigation



object Routes {
    const val LOGIN = "login"
    const val THREADS = "threads"
    const val CONVERSATION = "conversation"

    fun conversation(threadId: Int): String =
        "$CONVERSATION/$threadId"
}

package com.example.agent.ui.utility


object AuthErrorMapper {

    fun map(raw: String?): String {
        return when {
            raw == null ->
                "Something went wrong. Please try again."

            raw.contains("401") ||
                    raw.contains("invalid", true) ->
                "Incorrect email or password. Please try again."

            raw.contains("email", true) ->
                "This email is not registered."

            raw.contains("password", true) ->
                "The password you entered is incorrect."

            raw.contains("network", true) ||
                    raw.contains("timeout", true) ->
                "No internet connection. Check your network."

            else ->
                "Login failed. Please try again."
        }
    }
}

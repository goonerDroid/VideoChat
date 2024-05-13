package com.sublime.videochat.util

object UserHelper {

    fun generateRandomString(length: Int = 8, upperCaseOnly: Boolean = false): String {
        val allowedChars: List<Char> = ('A'..'Z') + ('0'..'9') + if (!upperCaseOnly) {
            ('a'..'z')
        } else {
            emptyList()
        }

        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getUserIdFromEmail(email: String) = email.replace(" ", "")
        .replace(".", "")
        .replace("@", "")
}
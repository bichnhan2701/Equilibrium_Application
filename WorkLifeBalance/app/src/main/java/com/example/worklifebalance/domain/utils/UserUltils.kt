package com.example.worklifebalance.domain.utils

import kotlin.collections.joinToString
import kotlin.collections.takeLast
import kotlin.text.split
import kotlin.text.toRegex
import kotlin.text.trim

fun getLastTwoWords(name: String): String {
    val words = name.trim().split("\\s+".toRegex())
    return if (words.size >= 2) {
        words.takeLast(2).joinToString(" ")
    } else {
        name
    }
}
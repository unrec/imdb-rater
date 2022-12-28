package com.unrec.imdb.rater.utils

import com.unrec.imdb.rater.Application
import com.unrec.imdb.rater.exception.InputStreamException
import java.io.InputStream

fun String.toInputStream(): InputStream {
    return Application::class.java.classLoader.getResourceAsStream(this)
        ?: throw InputStreamException(this)
}

fun Iterable<String>?.toSingleString() = this?.joinToString { it } ?: ""

fun String.reformatTitleTypeName(): String {
    return this
        .lowercase()
        .replace('_', ' ')
        .replace("tv", "TV")
        .capitalizeWords()
}

fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { it.replaceFirstChar(transform = Char::uppercaseChar) }
}

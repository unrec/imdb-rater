package com.unrec.imdb.rater.exception

class InputStreamException(path: String) : RuntimeException("Failed to read a file from the path = $path")

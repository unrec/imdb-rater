package com.unrec.imdb.rater.model

data class RateStatistics(
    val totalItems: Int? = null,
    val totalRuntime: Int? = null,
    val typesCount: List<TitleTypeStatistics>? = null,
    val mostWatchedYear: YearStatistics? = null,
    val mostWatchedGenres: List<String>? = null,
    val mostWatchedDirectors: List<DirectorStatistics>? = null
)

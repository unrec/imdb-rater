package com.unrec.imdb.rater.model

import java.math.BigDecimal

data class UserRateStatistics(
    val totalItems: Int? = null,
    val totalRuntime: Int? = null,
    val averageDeviation: BigDecimal? = null,
    val releaseYearRated: List<YearStatistics>? = null,
    val ratingCount: List<RatingStatistics>? = null,
    val typesCount: List<TitleTypeStatistics>? = null,
    val releaseYearCount: List<YearStatistics>? = null,
    val rateYearCount: List<YearStatistics>? = null,
    val mostWatchedYear: YearStatistics? = null,
    val mostWatchedGenres: List<String>? = null,
    val mostWatchedDirectors: List<DirectorStatistics>? = null
)

package com.unrec.imdb.rater.model

import java.math.BigDecimal

data class YearStatistics(
    val year: Short,
    val count: Int,
    val averageRating: BigDecimal
)

package com.unrec.imdb.rater.model

data class YearStatistics(
    val year: Short,
    val count: Int,
    val averageRating: Float
)

package com.unrec.imdb.rater.model

data class SearchParams(
    val rating: Byte?,
    val year: Short?,
    val title: String?,
    val director: String?
)

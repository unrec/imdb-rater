package com.unrec.imdb.rater.model

import com.unrec.imdb.csv.parser.TitleType
import java.time.LocalDate

data class RatedItem(
    val id: String? = null,
    val userRating: Byte? = null,
    val dateRated: LocalDate? = null,
    val title: String? = null,
    val url: String? = null,
    val titleType: TitleType? = null,
    val imdbRating: Float? = null,
    val runtime: Int? = null,
    val year: Short? = null,
    val genres: List<String>? = null,
    val votes: Int? = null,
    val releaseDate: LocalDate? = null,
    val directors: List<String>? = null
) {

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other?.let { id == (it as RatedItem).id } ?: false
    }
}

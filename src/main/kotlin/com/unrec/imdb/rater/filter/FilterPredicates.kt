package com.unrec.imdb.rater.filter

import com.unrec.imdb.csv.parser.ParsedItem
import com.unrec.imdb.rater.model.SearchParams

typealias FilterPredicate = (ParsedItem, SearchParams) -> Boolean

object FilterPredicates {

    private val ratingPredicate: FilterPredicate =
        { item, params -> params.rating trueIfNullOr { it == item.userRating } }

    private val yearPredicate: FilterPredicate =
        { item, params -> params.year trueIfNullOr { it == item.year } }

    private val titlePredicate: FilterPredicate =
        { item, params -> params.title trueIfNullOr { it == item.title } }

    private val directorPredicate: FilterPredicate =
        { item, params -> params.director trueIfNullOr { item.directors?.contains(it) ?: false } }

    @JvmStatic
    val predicates = listOf(ratingPredicate, yearPredicate, titlePredicate, directorPredicate)
}

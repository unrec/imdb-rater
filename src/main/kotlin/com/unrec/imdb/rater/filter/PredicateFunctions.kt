package com.unrec.imdb.rater.filter

import com.unrec.imdb.csv.parser.ParsedItem
import com.unrec.imdb.rater.model.SearchParams

inline infix fun <T> T?.trueIfNullOr(predicate: (T) -> Boolean): Boolean = when {
    this == null -> true
    else -> predicate(this)
}

fun Iterable<ParsedItem>.filterAll(
    params: SearchParams,
    predicates: List<FilterPredicate>
):
    List<ParsedItem> {
    return this.filter { candidate ->
        predicates.all { it(candidate, params) }
    }
}

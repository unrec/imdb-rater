package com.unrec.imdb.rater.controller

import com.unrec.imdb.rater.model.RatedItem
import com.unrec.imdb.rater.model.SearchParams
import com.unrec.imdb.rater.service.RatingService
import com.unrec.imdb.rater.utils.toSingleString
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class RatingController(private val ratingService: RatingService) {

    @QueryMapping
    fun find(
        @Argument filepath: String,
        @Argument params: SearchParams
    ): List<RatedItem> {
        return ratingService.filter(params, filepath)
    }

    @QueryMapping
    fun compare(
        @Argument aFilepath: String,
        @Argument bFilepath: String,
        @Argument params: SearchParams
    ): List<RatedItem> {
        return ratingService.compare(params, aFilepath, bFilepath)
    }

    @SchemaMapping
    fun genres(item: RatedItem) = item.genres.toSingleString()

    @SchemaMapping
    fun directors(item: RatedItem) = item.directors.toSingleString()

    private fun Iterable<String>?.toSingleString() =
        this?.joinToString { it } ?: ""
}



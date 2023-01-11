package com.unrec.imdb.rater.controller

import com.unrec.imdb.rater.model.UserRateStatistics
import com.unrec.imdb.rater.service.RatingService
import com.unrec.imdb.rater.utils.minutesToDurationString
import com.unrec.imdb.rater.utils.toSingleString
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class StatisticsController(private val ratingService: RatingService) {

    @QueryMapping
    fun statistics(@Argument filepath: String): UserRateStatistics {
        return ratingService.getStatistics(filepath)
    }

    @SchemaMapping
    fun totalRuntime(statistics: UserRateStatistics) = statistics.totalRuntime.minutesToDurationString()

    @SchemaMapping
    fun mostWatchedGenres(statistics: UserRateStatistics) = statistics.mostWatchedGenres.toSingleString()
}

package com.unrec.imdb.rater.controller

import com.unrec.imdb.rater.model.RateStatistics
import com.unrec.imdb.rater.service.RatingService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class StatisticsController(private val ratingService: RatingService) {

    @QueryMapping
    fun statistics(@Argument filepath: String): RateStatistics {
        return ratingService.getStatistics(filepath)
    }
}

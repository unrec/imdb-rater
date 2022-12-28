package com.unrec.imdb.rater.service

import com.unrec.imdb.csv.parser.ImdbCsvParser
import com.unrec.imdb.csv.parser.ParsedItem
import com.unrec.imdb.rater.filter.FilterPredicates.predicates
import com.unrec.imdb.rater.filter.filterAll
import com.unrec.imdb.rater.model.DirectorStatistics
import com.unrec.imdb.rater.model.RateStatistics
import com.unrec.imdb.rater.model.RatedItem
import com.unrec.imdb.rater.model.SearchParams
import com.unrec.imdb.rater.model.TitleTypeStatistics
import com.unrec.imdb.rater.model.toModel
import com.unrec.imdb.rater.utils.getOrZero
import com.unrec.imdb.rater.utils.minutesToDurationString
import com.unrec.imdb.rater.utils.reformatTitleTypeName
import com.unrec.imdb.rater.utils.toInputStream
import org.springframework.stereotype.Service

@Service
class RatingService {

    private val parser = ImdbCsvParser()

    fun filter(params: SearchParams, filepath: String): List<RatedItem> {
        return filepath.toInputStream()
            .let { parser.parse(it) }
            .filterAll(params, predicates)
            .map { it.toModel() }
    }

    fun compare(params: SearchParams, aFilepath: String, bFilepath: String): List<RatedItem> {
        val aItems = aFilepath.toInputStream()
            .let { parser.parse(it) }
            .filterAll(params, predicates)
            .map { it.toModel() }

        val bItems = bFilepath.toInputStream()
            .let { parser.parse(it) }
            .filterAll(params, predicates)
            .map { it.toModel() }

        return aItems.filter { bItems.contains(it) }
    }

    fun getStatistics(filepath: String): RateStatistics {
        val items: List<ParsedItem> = filepath.toInputStream()
            .let { parser.parse(it) }

        return RateStatistics(
            totalItems = items.size,
            totalRuntime = items.sumRuntime().minutesToDurationString(),
            typesCount = items.countTypes(),
            mostWatchedYear = items.mostWatchedYear(),
            mostWatchedGenres = items.mostWatchedGenres(),
            mostWatchedDirectors = items.mostWatchedDirectors()
        )
    }

    private fun List<ParsedItem>.sumRuntime(): Int {
        return this.sumOf { it.runtime.getOrZero() }
    }

    private fun List<ParsedItem>.countTypes(): List<TitleTypeStatistics> {
        return this
            .groupingBy { it.titleType!! }
            .eachCount()
            .toList()
            .map { TitleTypeStatistics(it.first.name.reformatTitleTypeName(), it.second) }
    }

    private fun List<ParsedItem>.mostWatchedYear(): Short? {
        return this
            .groupingBy { it.year }
            .eachCount()
            .maxByOrNull { it.value }?.key
    }

    private fun List<ParsedItem>.mostWatchedGenres(): List<String> {
        return this
            .flatMap { it.genres.orEmpty() }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedBy { (_, value) -> value }
            .takeLast(5)
            .map { it.first }
    }

    private fun List<ParsedItem>.mostWatchedDirectors(): List<DirectorStatistics> {
        return this
            .flatMap { it.directors.orEmpty() }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedBy { (_, value) -> value }
            .takeLast(5)
            .map { DirectorStatistics(it.first, it.second) }
    }
}
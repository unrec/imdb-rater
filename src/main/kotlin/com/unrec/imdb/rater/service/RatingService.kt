package com.unrec.imdb.rater.service

import com.unrec.imdb.csv.parser.ImdbCsvParser
import com.unrec.imdb.csv.parser.ParsedItem
import com.unrec.imdb.rater.filter.FilterPredicates.predicates
import com.unrec.imdb.rater.filter.filterAll
import com.unrec.imdb.rater.model.DirectorStatistics
import com.unrec.imdb.rater.model.RateStatistics
import com.unrec.imdb.rater.model.RatedItem
import com.unrec.imdb.rater.model.RatingStatistics
import com.unrec.imdb.rater.model.SearchParams
import com.unrec.imdb.rater.model.TitleTypeStatistics
import com.unrec.imdb.rater.model.YearStatistics
import com.unrec.imdb.rater.model.toModel
import com.unrec.imdb.rater.utils.getOrZero
import com.unrec.imdb.rater.utils.reformatTitleTypeName
import com.unrec.imdb.rater.utils.toInputStream
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

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
            totalRuntime = items.sumRuntime(),
            typesCount = items.countTypes(),
            ratingCount = items.countByRating(),
            releaseYearCount = items.countByReleaseYear(),
            rateYearCount = items.countPerRateYear(),
            mostWatchedYear = items.mostWatchedYear(),
            mostWatchedGenres = items.mostWatchedGenres(),
            mostWatchedDirectors = items.mostWatchedDirectors()
        )
    }

    private fun List<ParsedItem>.sumRuntime(): Int {
        return this.sumOf { it.runtime.getOrZero() }
    }

    private fun List<ParsedItem>.countByRating(): List<RatingStatistics> {
        return this
            .groupingBy { it.userRating!! }
            .eachCount()
            .toList()
            .sortedBy { (rating, _) -> rating }
            .map { RatingStatistics(it.first.toShort(), it.second) }
    }

    private fun List<ParsedItem>.countTypes(): List<TitleTypeStatistics> {
        return this
            .groupingBy { it.titleType!! }
            .eachCount()
            .toList()
            .map { TitleTypeStatistics(it.first.name.reformatTitleTypeName(), it.second) }
    }

    private fun List<ParsedItem>.countByReleaseYear(): List<YearStatistics> {
        return this
            .groupingBy { it.year!! }
            .eachCount()
            .toList()
            .sortedBy { (year, _) -> year }
            .map { YearStatistics(it.first, it.second, this.countAverageByYear(it.first)) }
    }

    private fun List<ParsedItem>.countPerRateYear(): List<YearStatistics> {
        return this
            .groupingBy { it.dateRated!!.year }
            .eachCount()
            .toList()
            .sortedBy { (year, _) -> year }
            .map { YearStatistics(it.first.toShort(), it.second, this.countAverageByYear(it.first.toShort())) }
    }

    private fun List<ParsedItem>.mostWatchedYear(): YearStatistics {
        val (year, count) = this
            .groupingBy { it.year!! }
            .eachCount()
            .maxByOrNull { it.value }!!

        return YearStatistics(year = year, count = count, averageRating = this.countAverageByYear(year))
    }

    private fun List<ParsedItem>.countAverageByYear(year: Short): BigDecimal {
        return this
            .filter { it.year == year }
            .map { it.userRating!! }
            .average()
            .toBigDecimal().setScale(2, RoundingMode.HALF_DOWN)
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
            .map { DirectorStatistics(name = it.first, count = it.second, titles = this.titlesByDirector(it.first)) }
    }

    private fun List<ParsedItem>.titlesByDirector(directorName: String): List<RatedItem> {
        return this
            .filter { it.directors!!.contains(directorName) }
            .sortedBy { it.year }
            .map { it.toModel() }

    }
}

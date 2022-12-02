package com.unrec.imdb.rater.service

import com.unrec.imdb.csv.parser.ImdbCsvParser
import com.unrec.imdb.rater.model.toModel
import com.unrec.imdb.rater.exception.InputStreamException
import com.unrec.imdb.rater.filter.FilterPredicates.predicates
import com.unrec.imdb.rater.filter.filterAll
import com.unrec.imdb.rater.model.RatedItem
import com.unrec.imdb.rater.model.SearchParams
import org.springframework.stereotype.Service
import java.io.InputStream

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
            .map{ it.toModel()}

        val bItems = bFilepath.toInputStream()
            .let { parser.parse(it) }
            .filterAll(params, predicates)
            .map { it.toModel() }

        return aItems.filter { bItems.contains(it) }
    }

    private fun String.toInputStream(): InputStream {
        return RatingService::class.java.classLoader.getResourceAsStream(this)
            ?: throw InputStreamException(this)
    }
}

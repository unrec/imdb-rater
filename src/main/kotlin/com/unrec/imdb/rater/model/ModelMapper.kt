package com.unrec.imdb.rater.model

import com.unrec.imdb.csv.parser.ParsedItem

fun ParsedItem.toModel() = RatedItem(
    id = id,
    userRating = userRating,
    dateRated = dateRated,
    title = title,
    url = url,
    titleType = titleType,
    imdbRating = imdbRating,
    runtime = runtime,
    year = year,
    genres = genres,
    votes = votes,
    releaseDate = releaseDate,
    directors = directors
)


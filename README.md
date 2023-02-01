# imdb-rater

Microservice for analyzing provided IMDb .csv rating files.

IMDB .csv can be downloaded here - https://www.imdb.com/user/{userId}/ratings/export.

### Supported queries
 - **statistics** ([query](docs/queries/statistics_query.graphql), [response](docs/responses/statistics_response.json))
 - **compare** ([query](docs/queries/compare_query.graphql), [response](docs/responses/compare_response.json))
 - **find** ([query](docs/queries/find_query.graphql), [response](docs/responses/find_response.json))

Currently .csv files should be located in `resource` folder. 

package com.unrec.imdb.rater.exception

import graphql.ErrorType
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.stereotype.Component

@Component
class CustomExceptionResolver : DataFetcherExceptionResolverAdapter() {

    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        return when (ex) {
            is InputStreamException -> {
                GraphqlErrorBuilder.newError()
                    .message(ex.message)
                    .location(env.field.sourceLocation)
                    .path(env.executionStepInfo.path)
                    .errorType(ErrorType.DataFetchingException)
                    .build()
            }
            else -> null
        }
    }
}

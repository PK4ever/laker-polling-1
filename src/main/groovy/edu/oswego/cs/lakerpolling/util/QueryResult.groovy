package edu.oswego.cs.lakerpolling.util

import org.springframework.http.HttpStatus


class QueryResult<T> {

    boolean success = true
    int errorCode = HttpStatus.OK.value()
    String message = HttpStatus.OK.reasonPhrase
    T data = null

    static QueryResult fromHttpStatus(HttpStatus status, QueryResult results = new QueryResult()) {
        results.with {
            success = false
            errorCode = status.value()
            message = status.reasonPhrase
        }
        results
    }

    static QueryResult copyError(QueryResult result, QueryResult copy = new QueryResult()) {
        copy.success = result.success
        copy.errorCode = result.errorCode
        copy.message = result.message
        copy
    }
}

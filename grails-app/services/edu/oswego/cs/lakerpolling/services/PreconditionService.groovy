package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.domains.AuthToken
import edu.oswego.cs.lakerpolling.util.QueryResult
import grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.http.HttpStatus

/**
 * Precondition checking service.
 */
class PreconditionService {

    /**
     * Checks that all given parameters exist in params map. Stops upon the first failure.
     * @param paramsMap - The parameters map for the request.
     * @param parameters - The required parameters.
     * @param results - An optional parameter. Results will be recorded in this object.
     * @return A results object containing check results.
     */
    QueryResult notNull(GrailsParameterMap paramsMap, List<String> parameters, QueryResult results = new QueryResult(success: true)) {

        if (!results.success) {
            return results
        }

        if (parameters.size() > 0) {

            for (String param in parameters) {
                if (!paramsMap.containsKey(param)) {
                    results.with {
                        success = false
                        errorCode = HttpStatus.BAD_REQUEST.value()
                        message = "Required parameter $param is missing"
                    }
                    break
                }
            }

        }
        results
    }

    /**
     * Retrieves an {@link AuthToken} object for the given accessTokenString if one exists.
     * @param accessTokenString The access token string to search by.
     * @param results - An optional parameter. Results will be recorded in this object.
     * @return A query result.
     */
    QueryResult<AuthToken> accessToken(String accessTokenString, QueryResult<AuthToken> results = new QueryResult<>(success: true)) {

        if (!results.success) {
            return results
        }
        if(accessTokenString == null)
            throw IllegalArgumentException
        AuthToken token = AuthToken.findByAccessToken(accessTokenString)

        if (token != null) {
            results.data = token
        } else {
            QueryResult.fromHttpStatus(HttpStatus.UNAUTHORIZED, results)
        }

        results
    }

    QueryResult<Long> convertToLong(String rawValue, String paramName, QueryResult<Long> results = new QueryResult<>(success: true)) {
        if (!results.success) {
            return results
        }

        if (rawValue != null && rawValue.isLong()) {
            results.data = rawValue.toLong()
        } else {
            results.success = false
            results.message = "Param $paramName should be a number. Value received: $rawValue"
        }
        results
    }

}

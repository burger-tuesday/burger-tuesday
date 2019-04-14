package com.grosslicht.burgertuesday.controller.util

import org.springframework.data.domain.Page
import org.springframework.http.HttpHeaders
import org.springframework.web.util.UriComponentsBuilder
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * Utility class for handling pagination.
 *
 * Pagination uses the same principles as the [GitHub API](https://developer.github.com/v3/#pagination),
 * and follow [RFC 5988 (Link header)](http://tools.ietf.org/html/rfc5988).
 */
object PaginationUtil {

    fun <T> generatePaginationHttpHeaders(page: Page<T>, baseUrl: String): HttpHeaders {

        val headers = HttpHeaders()
        headers.add("X-Total-Count", java.lang.Long.toString(page.totalElements))
        var link = ""
        if (page.number + 1 < page.totalPages) {
            link = "<" + generateUri(baseUrl, page.number + 1, page.size) + ">; rel=\"next\","
        }
        // prev link
        if (page.number > 0) {
            link += "<" + generateUri(baseUrl, page.number - 1, page.size) + ">; rel=\"prev\","
        }
        // last and first link
        var lastPage = 0
        if (page.totalPages > 0) {
            lastPage = page.totalPages - 1
        }
        link += "<" + generateUri(baseUrl, lastPage, page.size) + ">; rel=\"last\","
        link += "<" + generateUri(baseUrl, 0, page.size) + ">; rel=\"first\""
        headers.add(HttpHeaders.LINK, link)
        return headers
    }

    private fun generateUri(baseUrl: String, page: Int, size: Int): String {
        return UriComponentsBuilder
            .fromUriString(baseUrl).queryParam("page", page).queryParam("size", size).toUriString()
    }

    fun <T> generateSearchPaginationHttpHeaders(
        query: String,
        page: Page<T>,
        baseUrl: String
    ): HttpHeaders {
        val escapedQuery: String
        try {
            escapedQuery = URLEncoder.encode(query, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException(e)
        }

        val headers = HttpHeaders()
        headers.add("X-Total-Count", java.lang.Long.toString(page.totalElements))
        var link = ""
        if (page.number + 1 < page.totalPages) {
            link = "<" + generateUri(
                baseUrl,
                page.number + 1,
                page.size
            ) + "&query=" + escapedQuery + ">; rel=\"next\","
        }
        // prev link
        if (page.number > 0) {
            link += "<" + generateUri(
                baseUrl,
                page.number - 1,
                page.size
            ) + "&query=" + escapedQuery + ">; rel=\"prev\","
        }
        // last and first link
        var lastPage = 0
        if (page.totalPages > 0) {
            lastPage = page.totalPages - 1
        }
        link += "<" + generateUri(
            baseUrl,
            lastPage,
            page.size
        ) + "&query=" + escapedQuery + ">; rel=\"last\","
        link += "<" + generateUri(
            baseUrl,
            0,
            page.size
        ) + "&query=" + escapedQuery + ">; rel=\"first\""
        headers.add(HttpHeaders.LINK, link)
        return headers
    }
}

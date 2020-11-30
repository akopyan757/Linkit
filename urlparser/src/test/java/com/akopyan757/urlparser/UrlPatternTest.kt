package com.akopyan757.urlparser

import org.junit.Test

class UrlPatternTest {

    companion object {
        private const val TEST_URL_1 = "https://www.championat.com/football/article-4194439-kak-arsen-venger-rabotal-v-japonii-maloizvestnaja-stranica-v-biografii-velikogo-trenera.html"
        private const val TEST_PATTERN_1 = "((http|https)://www.championat.com/[a-z]{2,16}/)article-.*"
    }

    @Test
    fun parse1() {
        val pattern = UrlPattern()
        val baseUrl = pattern.isMatchPattern(TEST_PATTERN_1, TEST_URL_1)
        assert(baseUrl)
    }

    @Test
    fun parse2() {
        val pattern = UrlPattern()
        val baseUrl = pattern.getBaseUrl(TEST_URL_1)
        assert(baseUrl == "www.championat.com")
    }
}
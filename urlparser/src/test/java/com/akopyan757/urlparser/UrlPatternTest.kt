package com.akopyan757.urlparser

import org.junit.Test

class UrlPatternTest {

    companion object {
        private const val TEST_URL_1 = "https://www.championat.com/football/article-4194439-kak-arsen-venger-rabotal-v-japonii-maloizvestnaja-stranica-v-biografii-velikogo-trenera.html"
        private const val TEST_PATTERN_1 = "((http|https)://www.championat.com/[a-z]{2,16}/)article-.*"

        private const val TEST_URL_2 = "https://ru.wikipedia.org/wiki/%D0%90%D0%BD%D0%B4%D1%81%D0%BA%D0%B8%D0%B9_%D1%84%D0%BB%D0%B0%D0%BC%D0%B8%D0%BD%D0%B3%D0%BE"
        private const val TEST_PATTERN_2 = "^https?://[a-z]{2}.(m.)?wikipedia.org/wiki/.*\$"
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

    @Test
    fun parse3() {
        val pattern = UrlPattern()
        val baseUrl = pattern.isMatchPattern(TEST_PATTERN_2, TEST_URL_2)
        assert(baseUrl)
    }
}
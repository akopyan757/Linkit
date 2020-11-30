package com.akopyan757.urlparser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UrlParserTest {

    class ExamplePatternCache: IPatternCache<PatternData> {
        override fun getBasePattern(baseUrl: String) = PatternData(
            "^.*/www.championat.com/.*",
            true,
            null,
            null,
            "head.link.icon.image/png.48x48"
        )

        override fun getSpecificPatterns(baseUrl: String) = emptyList<PatternData>()
        override fun insertOrUpdate(data: PatternData) {}
        override fun removeByFolder(baseUrl: String) {}
        override fun removeAll() {}
    }

    class TestUrlDataFactory: IUrlDataFactory<TestUrlData> {
        override fun createData() = TestUrlData()
    }

    companion object {
        private const val TEST_URL_1 = "https://www.championat.com/football/article-4194439-kak-arsen-venger-rabotal-v-japonii-maloizvestnaja-stranica-v-biografii-velikogo-trenera.html"
        private const val TEST_URL_2 = "https://yandex.ru/"
    }

    private lateinit var patternCache: IPatternCache<PatternData>
    private lateinit var urlParser: UrlParser<PatternData, TestUrlData>
    private lateinit var urlDataFactory: TestUrlDataFactory

    @Before
    fun before() {
        patternCache = ExamplePatternCache()
        urlDataFactory = TestUrlDataFactory()
        urlParser = UrlParser(patternCache, urlDataFactory)
    }

    @Test
    fun test1() = runBlocking(Dispatchers.IO) {
        val data = urlParser.parseUrl(TEST_URL_1)
        data.log()
        assert(true)
    }


    @Test
    fun test2() = runBlocking(Dispatchers.IO) {
        patternCache = ExamplePatternCache()
        urlParser = UrlParser(patternCache, urlDataFactory)
        val data = urlParser.parseUrl(TEST_URL_2)
        data.log()
        assert(true)
    }

    private fun TestUrlData.log() {
        println("UrlData(" +
            "url=${dataUrl}, " +
            "title=${dataTitle}, " +
            "description=${dataDescription}, " +
            "imageContentUrl=${dataImageContentUrl}, " +
            "logoContentUrl=${dataLogoContentUrl}" +
        ")")
    }
}
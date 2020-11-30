package com.akopyan757.urlparser

import java.lang.reflect.Type

class UrlParser<T: IPatternData, V: UrlData>(
    private val patternCache: IPatternCache<T>,
    private val factory: IUrlDataFactory<V>,
    private val documentSearch: IDocumentSearch = DocumentSearchImpl(),
) {

    private val mUrlPattern = UrlPattern()

    fun loadPatternsFromJson(type: Type) {
        val patterns = JsonPatternsParser.parse<T>("pattern.json", type)
        patternCache.removeAll()
        patterns.forEach { item ->
            println("INSERT PATTERN: $item")
            patternCache.insertOrUpdate(item)
        }
    }

    suspend fun parseUrl(url: String): V {
        documentSearch.request(url)

        val baseUrl = mUrlPattern.getBaseUrl(url)
        val baseUrlFormat = "%$baseUrl%"
        println("BaseUrl = $baseUrl")

        val basePattern = baseUrl?.let { patternCache.getBasePattern(baseUrlFormat) }
        val specifiedPatterns = baseUrl?.let { patternCache.getSpecificPatterns(baseUrlFormat) }

        println("1. $basePattern")
        println("2. ${specifiedPatterns?.joinToString(", ")}")

        val basePatternData = basePattern?.takeIf { pattern ->
            mUrlPattern.isMatchPattern(pattern.pattern(), url)
        }

        val specifiedPatternData = specifiedPatterns?.find { pattern ->
            mUrlPattern.isMatchPattern(pattern.pattern(), url)
        }

        println("BasePattern = $basePatternData")
        println("SpecifiedPatterns = $specifiedPatternData")

        val titleElement = specifiedPatternData?.titleElement()?.let { ElementType.parse(it) }
            ?: basePattern?.titleElement()?.let { ElementType.parse(it) }
            ?: ElementType.Title

        val descriptionElement = specifiedPatternData?.descriptionElement()?.let { ElementType.parse(it) }
            ?: basePattern?.descriptionElement()?.let { ElementType.parse(it) }
            ?: ElementType.MetaDescription

        val imageElement = specifiedPatternData?.imageUrlElement()?.let { ElementType.parse(it) }
        val logoElement = basePatternData?.imageUrlElement()?.let { ElementType.parse(it) }

        return factory.createData().apply {
            this.dataUrl = url
            this.dataTitle = titleElement.getData(documentSearch)
            this.dataDescription = descriptionElement.getData(documentSearch)
            this.dataImageContentUrl = imageElement?.getData(documentSearch)
            this.dataLogoContentUrl = logoElement?.getData(documentSearch)
            println(this)
        }
    }
}
package com.akopyan757.urlparser

import com.akopyan757.urlparser.data.IPatternHostData
import com.akopyan757.urlparser.data.IPatternSpecifiedData

class UrlParser<T, P, V>(
    private val patternCache: IPatternCache<T, P>,
    private val factory: IUrlDataFactory<V>,
    private val documentSearch: IDocumentSearch = DocumentSearchImpl(),
) where T : IPatternHostData,
        T : IPatternSpecifiedData,
        P : IPatternHostData,
        V: UrlData
{

    private val mUrlPattern = UrlPattern()

    suspend fun parseUrl(url: String): V? {
        documentSearch.request(url)

        val baseUrl = mUrlPattern.getBaseUrl(url) ?: return null

        println("BaseUrl = $baseUrl")

        val patterns = patternCache.getPatterns(baseUrl)

        return if (patterns.isNotEmpty()) {
            val pattern = patterns.find { mUrlPattern.isMatchPattern(it.host(), url) }

            val titleElement = pattern?.specTitleElement().toElementType()
                ?: pattern?.hostTitleElement().toElementType(ElementType.Title)

            val descriptionElement = pattern?.specDescriptionElement().toElementType()
                ?: pattern?.hostDescriptionElement().toElementType(ElementType.MetaDescription)

            val imageElement = pattern?.specImageUrlElement().toElementType()
            val logoElement = pattern?.hostImageUrlElement().toElementType()

            factory.createData().apply {
                this.dataUrl = url
                this.dataTitle = titleElement.getData(documentSearch)
                this.dataDescription = descriptionElement.getData(documentSearch)
                this.dataImageContentUrl = imageElement?.getData(documentSearch)
                this.dataLogoContentUrl = logoElement?.getData(documentSearch)
                println(this)
            }
        } else {
            val hostPatterns = patternCache.getHostPatterns(baseUrl)
            val pattern = hostPatterns.find { mUrlPattern.isMatchPattern(it.host(), url) }

            val titleElement = pattern?.hostTitleElement().toElementType(ElementType.Title)
            val descriptionElement = pattern?.hostDescriptionElement().toElementType(ElementType.MetaDescription)
            val logoElement = pattern?.hostImageUrlElement().toElementType()

            factory.createData().apply {
                this.dataUrl = url
                this.dataTitle = titleElement.getData(documentSearch)
                this.dataDescription = descriptionElement.getData(documentSearch)
                this.dataLogoContentUrl = logoElement?.getData(documentSearch)
                println(this)
            }
        }
    }
}
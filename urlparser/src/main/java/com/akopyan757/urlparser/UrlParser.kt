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

        val patternList = patternCache.getPatterns(baseUrl)
        val pattern = patternList.find { mUrlPattern.isMatchPattern(it.host(), url) }

        val patternData = factory.createData()
        patternData.dataUrl = url

        if (pattern != null) {

            val titleElement = pattern.specTitleElement().toElementType()
                ?: pattern.hostTitleElement().toElementType(ElementType.Title)

            val descriptionElement = pattern.specDescriptionElement().toElementType()
                ?: pattern.hostDescriptionElement().toElementType(ElementType.MetaDescription)

            val imageElement = pattern.specImageUrlElement().toElementType()
            val logoElement = pattern.hostImageUrlElement().toElementType()

            patternData.dataTitle = titleElement.getData(documentSearch)
            patternData.dataDescription = descriptionElement.getData(documentSearch)
            patternData.dataImageContentUrl = imageElement?.getData(documentSearch)
            patternData.dataLogoContentUrl = logoElement?.getData(documentSearch)
            patternData.patternHostId = pattern.specHostId()
            patternData.patternSpecifiedId = pattern.specId()
        } else {
            val hostPatterns = patternCache.getHostPatterns(baseUrl)
            val patternHost = hostPatterns.find { mUrlPattern.isMatchPattern(it.host(), url) }

            if (patternHost != null) {
                val titleElement = pattern?.hostTitleElement().toElementType(ElementType.Title)
                val descriptionElement = pattern?.hostDescriptionElement().toElementType(ElementType.MetaDescription)
                val logoElement = pattern?.hostImageUrlElement().toElementType()

                patternData.dataTitle = titleElement.getData(documentSearch)
                patternData.dataDescription = descriptionElement.getData(documentSearch)
                patternData.dataLogoContentUrl = logoElement?.getData(documentSearch)
                patternData.patternHostId = patternHost.id()
            }
        }

        println(patternData)

        return patternData
    }
}
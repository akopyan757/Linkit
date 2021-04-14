package com.akopyan757.linkit_model.parser

import com.akopyan757.linkit_model.parser.tags.AdditionalHtmlTags
import com.akopyan757.linkit_model.parser.tags.HtmlTags
import com.akopyan757.linkit_model.parser.tags.OpenGraphHtmlTags
import com.akopyan757.linkit_model.parser.tags.TwitterHtmlTags
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * @author Albert Akopyan
 */
class HtmlParser: IHtmlParser {

    /** It is necessary to call in background thread */
    override fun parseHeadTagsFromResource(resourceUrl: String): HtmlTags {
        val document = loadHtmlDocument(resourceUrl)
        return parseHtmlTags(document)
    }

    private fun loadHtmlDocument(resourceUrl: String): Document {
        return Jsoup.connect(resourceUrl).userAgent(USER_AGENT).get()
    }

    private fun parseHtmlTags(document: Document): HtmlTags {
        val openGraphTags = parseOpenGraphTags(document)
        val twitterTags = parseTwitterTags(document)
        val additionalHtmlTags = parseAdditionalTags(document)
        return HtmlTags(openGraphTags, twitterTags, additionalHtmlTags)
    }

    private fun parseOpenGraphTags(document: Document): OpenGraphHtmlTags {
        val headTag = document.head()
        val ogTagsMap = headTag.getOgMetaPropertiesContentMap()
        return OpenGraphHtmlTags.fromMap(ogTagsMap)
    }

    private fun parseTwitterTags(document: Document): TwitterHtmlTags {
        val headTag = document.head()
        val twitterTagsMap = headTag.getMetaNamesContentMap(TwitterHtmlTags.TAG_TWITTER)
        return TwitterHtmlTags.fromMap(twitterTagsMap)
    }

    private fun parseAdditionalTags(document: Document): AdditionalHtmlTags {
        val headTag = document.head()
        val canonical = headTag.getLinkRel(AdditionalHtmlTags.TAG_CANONICAL)
        val description = headTag.getMetaNameContent(AdditionalHtmlTags.TAG_META_DESCRIPTION)
        val author = headTag.getMetaNameContent(AdditionalHtmlTags.TAG_META_AUTHOR)
        val title = document.title()
        return AdditionalHtmlTags(canonical, title, description, author)
    }

    private fun Element.getOgMetaPropertiesContentMap(): Map<String, String> {
        val fullHashMap = HashMap<String, String>()
        val ogTags = getMetaPropertiesContentMap(OpenGraphHtmlTags.TAG_OPEN_GRAPH)
        val articleTags = getMetaPropertiesContentMap(OpenGraphHtmlTags.TAG_ARTICLE)
        val fbTags = getMetaPropertiesContentMap(OpenGraphHtmlTags.TAG_FACEBOOK)
        fullHashMap.putAll(ogTags)
        fullHashMap.putAll(articleTags)
        fullHashMap.putAll(fbTags)
        return fullHashMap
    }

    private fun Element.getMetaPropertiesContentMap(property: String): Map<String, String> {
        val query = SELECT_PROPERTY.format(property)
        return select(query).associateBy(
            keySelector = { element -> element.getAttrProperty() },
            valueTransform = { element -> element.getAttrContent() }
        )
    }

    private fun Element.getMetaNamesContentMap(name: String): Map<String, String> {
        val query = SELECT_NAME.format(name)
        return select(query).associateBy(
            keySelector = { element -> element.getAttrName() },
            valueTransform = { element -> element.getAttrContent() }
        )
    }

    private fun Element.getMetaNameContent(name: String): String? {
        val query = SELECT_NAME.format(name)
        return select(query).first()?.attr(ATTR_CONTENT)
    }

    private fun Element.getLinkRel(rel: String): String? {
        val query = SELECT_LINK_REL.format(rel)
        return this.select(query).first()?.attr(ATTR_HREF)
    }

    private fun Element.getAttrProperty() = this.attr(ATTR_PROPERTY)
    private fun Element.getAttrName() = this.attr(ATTR_NAME)
    private fun Element.getAttrContent() = this.attr(ATTR_CONTENT)

    companion object {
        private const val SELECT_PROPERTY = "meta[property^=%s]"
        private const val SELECT_NAME = "meta[name^=%s]"
        private const val SELECT_LINK_REL = "link[rel^=%s]"

        private const val ATTR_PROPERTY = "property"
        private const val ATTR_NAME = "name"
        private const val ATTR_CONTENT = "content"
        private const val ATTR_HREF = "href"

        const val USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
    }
}
package com.akopyan757.urlparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DocumentSearchImpl: IDocumentSearch {

    private var mDocument: Document? = null
    private var mUrl: String? = null

    override suspend fun request(url: String) = suspendCoroutine<Boolean> { cont ->
        try {
            mUrl = url
            mDocument = Jsoup.connect(url)
                .userAgent(Config.USER_AGENT)
                .followRedirects(Config.FOLLOW_DIRECT)
                .get()
            cont.resume(true)
        } catch (e: IOException) {
            mUrl = null
            mDocument = null
            cont.resumeWithException(e)
        }
    }

    override fun getUrl() = mUrl ?: Config.EMPTY

    override fun getTitle() = mDocument?.title() ?: Config.EMPTY

    override fun getLinkIconHref(rel: String, type: String?, sizes: String?): String {
        val relValue = Config.ATTR_REL.format(rel)
        val typeValue = type?.let { Config.ATTR_TYPE.format(it) } ?: Config.EMPTY
        val sizesValue = sizes?.let { Config.ATTR_SIZES.format(it) } ?: Config.EMPTY
        return mDocument?.head()
            ?.select("${Config.TAG_LINK}$relValue$typeValue$sizesValue")?.first()
            ?.absUrl(Config.RES_HREF) ?: Config.EMPTY
    }

    override fun getMetaDescription() = getMetaByName(Config.DESCRIPTION)

    override fun getMetaKeywords() = getMetaByName(Config.KEYWORD)

    override fun getMetaAuthor() = getMetaByName(Config.AUTHOR)

    override fun getMetaByName(name: String): String {
        val nameValue = Config.ATTR_NAME.format(name)
        return mDocument?.head()
            ?.select("${Config.TAG_META}$nameValue")?.first()
            ?.absUrl(Config.RES_CONTENT) ?: Config.EMPTY
    }

    override fun getMetaByProperty(property: String): String {
        val nameValue = Config.ATTR_PROPERTY.format(property)
        return mDocument?.head()
            ?.select("${Config.TAG_META}$nameValue")?.first()
            ?.absUrl(Config.RES_CONTENT) ?: Config.EMPTY
    }
}
package com.akopyan757.urlparser

internal object Config {
    const val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36"
    const val EMPTY = ""
    const val FOLLOW_DIRECT = true

    const val TAG_LINK = "link"
    const val TAG_META = "meta"
    const val ATTR_REL = "[rel=%s]"
    const val ATTR_TYPE = "[type=%s]"
    const val ATTR_SIZES = "[sizes=%s]"
    const val ATTR_NAME = "[name=%s]"
    const val ATTR_PROPERTY = "[property=%s]"
    const val RES_CONTENT = "content"
    const val RES_HREF = "href"
    const val AUTHOR = "author"
    const val DESCRIPTION = "description"
    const val KEYWORD = "keyword"
    const val ICON = "icon"
}
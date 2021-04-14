package com.akopyan757.linkit_model.parser

import com.akopyan757.linkit_model.parser.tags.HtmlTags

interface IHtmlParser {
    fun parseHeadTagsFromResource(resourceUrl: String): HtmlTags
}
package com.akopyan757.urlparser

sealed class ElementType(val code: String) {

    object Title: ElementType(TITLE) {
        override fun getData(search: IDocumentSearch) = search.getTitle()
    }

    object MetaDescription: ElementType(DESCRIPTION) {
        override fun getData(search: IDocumentSearch) = search.getMetaDescription()
    }

    object MetaKeywords: ElementType(KEYWORDS) {
        override fun getData(search: IDocumentSearch) = search.getMetaKeywords()
    }

    object MetaAuthor: ElementType(AUTHOR) {
        override fun getData(search: IDocumentSearch) = search.getMetaAuthor()
    }

    data class MetaName(val name: String): ElementType(META_NAME) {
        override fun getData(search: IDocumentSearch) = search.getMetaByName(name)
        override fun getFormat() =  "${super.getFormat()}.$name"
    }

    data class MetaProperty(val property: String): ElementType(META_PROPERTY) {
        override fun getData(search: IDocumentSearch) = search.getMetaByProperty(property)
        override fun getFormat() = "${super.getFormat()}.$property"
    }

    data class LinkIconHref(
        val rel: String = Config.ICON,
        val type: String? = null,
        val sizes: String? = null
    ): ElementType(LINK) {
        override fun getData(search: IDocumentSearch) = search.getLinkIconHref(rel, type, sizes)
        override fun getFormat() = "${super.getFormat()}.$rel.${type ?: ""}.${sizes ?: ""}"
    }

    open fun getData(search: IDocumentSearch): String = ""
    open fun getFormat(): String = code

    companion object {
        private const val TITLE = "head.title"
        private const val DESCRIPTION = "head.meta.description"
        private const val KEYWORDS = "head.meta.keywords"
        private const val AUTHOR = "head.meta.author"
        private const val META_NAME = "head.meta.name"
        private const val META_PROPERTY = "head.meta.property"
        private const val LINK = "head.link"

        fun parse(value: String): ElementType? = when {
            value.startsWith(TITLE) -> Title
            value.startsWith(DESCRIPTION) -> MetaDescription
            value.startsWith(KEYWORDS) -> MetaKeywords
            value.startsWith(AUTHOR) -> MetaAuthor
            value.startsWith(META_NAME) -> MetaName(value.removePrefix("$META_NAME."))
            value.startsWith(META_PROPERTY) -> MetaProperty(value.removePrefix("$META_PROPERTY."))
            value.startsWith(LINK) -> {
                val values = value.removePrefix("$LINK.").split(".")
                val type = values[1].takeUnless { it.isEmpty() }
                val sizes = values[2].takeUnless { it.isEmpty() }
                LinkIconHref(values[0], type, sizes)
            }
            else -> null
        }
    }
}
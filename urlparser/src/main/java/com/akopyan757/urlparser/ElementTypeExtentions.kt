package com.akopyan757.urlparser

fun String?.toElementType(default: ElementType): ElementType {
    return if (this != null) ElementType.parse(this) ?: default else default
}

fun String?.toElementType(): ElementType? {
    return if (this != null) ElementType.parse(this) else null
}
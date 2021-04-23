package com.akopyan757.linkit_domain.entity

data class UrlLinkEntity(
    val id: String,
    val url: String,
    val title: String,
    val description: String,
    val photoUrl: String?,
    val logoUrl: String?,
    val folderId: String?,
    val site: String?,
    val type: Type,
    var googleApp: UrlLinkGoogleAppEntity?,
    val order: Int
) {
    constructor() : this(
        "", "", "", "", null, null,
        null, null, Type.DEFAULT, null,0
    )

    constructor(id: String, url: String, order: Int) : this(
        id, url, "", "", null, null,
        null, null, Type.DEFAULT, null, order
    )

    enum class Type(val value: String) {
        DEFAULT("default"),
        LARGE_CARD("large_card"),
        PLAYER("player");

        companion object {
            fun fromValue(value: String) = when (value) {
                LARGE_CARD.value -> LARGE_CARD
                PLAYER.value -> PLAYER
                DEFAULT.value -> DEFAULT
                else -> DEFAULT
            }
        }
    }
}
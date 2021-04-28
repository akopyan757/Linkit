package com.akopyan757.linkit_domain.entity

data class UrlLinkEntity(
    var id: String,
    val url: String,
    val title: String,
    val description: String,
    val photoUrl: String?,
    var folderId: String?,
    val site: String?,
    val type: Type,
    val app: UrlLinkAppEntity?,
    var order: Int,
    var collapsed: Boolean = false
) {
    constructor() : this(
        "", "", "", "", null,
        null, null, Type.DEFAULT, null,0, false
    )

    constructor(id: String, url: String, order: Int) : this(
        id, url, "", "", null,
        null, null, Type.DEFAULT, null, order, false
    )

    enum class Type(val value: String) {
        DEFAULT("default"),
        LARGE_CARD("large_card"),
        PLAYER("player"),
        CARD("card");

        companion object {
            fun fromValue(value: String) = when (value) {
                LARGE_CARD.value -> LARGE_CARD
                PLAYER.value -> PLAYER
                DEFAULT.value -> DEFAULT
                CARD.value -> CARD
                else -> DEFAULT
            }
        }
    }
}
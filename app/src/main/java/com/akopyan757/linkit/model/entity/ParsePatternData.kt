package com.akopyan757.linkit.model.entity

import androidx.room.Embedded
import androidx.room.Ignore
import com.akopyan757.urlparser.data.IPatternHostData
import com.akopyan757.urlparser.data.IPatternSpecifiedData

class ParsePatternData(
    @Embedded val hostData: PatternHostData,
    @Embedded val specifiedData: PatternSpecifiedData
): IPatternHostData, IPatternSpecifiedData {
    override fun id(): Int = hostData.id
    override fun host(): String = hostData.host
    override fun hostTitleElement(): String? = hostData.titleElement
    override fun hostDescriptionElement(): String? = hostData.descriptionElement
    override fun hostImageUrlElement(): String? = hostData.imageUrlElement
    override fun specId(): Int = specifiedData.id
    override fun specHostId(): Int = specifiedData.hostId
    override fun specPattern(): String = specifiedData.pattern
    override fun specTitleElement(): String? = specifiedData.titleElement
    override fun specDescriptionElement(): String? = specifiedData.descriptionElement
    override fun specImageUrlElement(): String? = specifiedData.descriptionElement
}
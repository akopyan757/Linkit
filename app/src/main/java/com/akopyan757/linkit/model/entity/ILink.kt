package com.akopyan757.linkit.model.entity

interface ILink {
    fun linkId(): Long
    fun linkGroupIds(): List<Int>
    fun linkOrder(): Int
}
package com.akopyan757.linkit.common.utils

import kotlin.math.roundToLong

/**
 * @author albert.akopyan,
 * date creation: 25.07.2019.
 */
object FormatUtils {

    private const val KILO = "K"
    private const val MEGA = "M"
    private const val BYTE = "B"

    private const val THOUSAND = 1_000L
    private const val MILLION = 1_000_000L

    private const val FILE_SIZE_FORMAT = "%.2f"
    private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    fun getItemsCount(count: Long): String {
        return when {
            count < THOUSAND -> count.toString()
            count < MILLION -> convertCountFormat(count, THOUSAND) + KILO
            else -> convertCountFormat(count, MILLION) + MEGA
        }
    }

    private fun convertCountFormat(count: Long, base: Long): String {
        val value = (count / base.toFloat() * 10).roundToLong()
        return if (value % 10 == 0L) {
            (value / 10).toString()
        } else {
            (value / 10.0).toString()
        }
    }

    fun getSize(size: Long): String {
        return when {
            size < THOUSAND -> size.toString() + BYTE
            size < MILLION -> FILE_SIZE_FORMAT.format(size.toDouble() / THOUSAND) + KILO + BYTE
            else -> FILE_SIZE_FORMAT.format(size.toDouble() / MILLION) + MEGA + BYTE
        }
    }
}
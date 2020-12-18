package com.akopyan757.urlparser

import java.util.regex.Pattern


internal class UrlPattern {

    companion object {
        private const val URL_REGEX = "(http|https)://((www.)?" +
            "?[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]{2,6})\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)"
    }

    fun getBaseUrl(url: String): String {
        val pattern = Pattern.compile(URL_REGEX)
        val matcher = pattern.matcher(url)
        return if (matcher.find()) {
            //(0 until matcher.groupCount()).forEach {
            //    println("Group $it. ${matcher.group(it)}")
            //}
            matcher.group(2)
        } else throw Exception("Url is not valid.")
    }

    fun isMatchPattern(pattern: String, url: String): Boolean {
        return if (isValidURL(url)) {
            val pattern = Pattern.compile(pattern)
            val matcher = pattern.matcher(url)
            if (matcher.find()) {
                (0 until matcher.groupCount()).forEach {
                    println("Group $it. ${matcher.group(it)}")
                }
            }
            matcher.matches()
        } else false
    }

    private fun isValidURL(url: String?): Boolean {
        if (url == null) return false
        val pattern = Pattern.compile(URL_REGEX)
        val matcher = pattern.matcher(url)
        return matcher.matches()
    }
}
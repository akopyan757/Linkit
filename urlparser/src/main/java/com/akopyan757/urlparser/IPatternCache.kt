package com.akopyan757.urlparser

import com.akopyan757.urlparser.data.IPatternHostData
import com.akopyan757.urlparser.data.IPatternSpecifiedData

interface IPatternCache<T, V>
        where V : IPatternHostData,
              T : IPatternHostData,
              T : IPatternSpecifiedData
{
    fun getPatterns(host: String): List<T>
    fun getHostPatterns(host: String): List<V>
}
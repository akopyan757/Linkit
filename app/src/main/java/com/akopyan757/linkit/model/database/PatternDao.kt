package com.akopyan757.linkit.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akopyan757.linkit.model.entity.PatternData
import com.akopyan757.urlparser.IPatternCache

@Dao
interface PatternDao: IPatternCache<PatternData> {

    @Query("SELECT * FROM pattern_data WHERE is_base AND pattern LIKE :baseUrl")
    override fun getBasePattern(baseUrl: String): PatternData?

    @Query("SELECT * FROM pattern_data WHERE NOT is_base AND pattern LIKE :baseUrl")
    override fun getSpecificPatterns(baseUrl: String): List<PatternData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertOrUpdate(data: PatternData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: List<PatternData>)

    @Query("DELETE from pattern_data WHERE pattern LIKE :baseUrl")
    override fun removeByFolder(baseUrl: String)

    @Query("DELETE FROM pattern_data")
    override fun removeAll()
}
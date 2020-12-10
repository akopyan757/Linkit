package com.akopyan757.linkit.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akopyan757.linkit.model.entity.ParsePatternData
import com.akopyan757.linkit.model.entity.PatternHostData
import com.akopyan757.linkit.model.entity.PatternSpecifiedData
import com.akopyan757.urlparser.IPatternCache

@Dao
interface PatternDao: IPatternCache<ParsePatternData, PatternHostData> {

    @Query("SELECT host.*, spec.* FROM pattern_host_data as host JOIN pattern_specified_data as spec ON host.host_id = spec.spec_host_id WHERE :host LIKE '%' || host.host || '%'")
    override fun getPatterns(host: String): List<ParsePatternData>

    @Query("SELECT * FROM pattern_host_data WHERE :host LIKE '%' || host || '%'")
    override fun getHostPatterns(host: String): List<PatternHostData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHostOrUpdate(data: PatternHostData): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSpecifiedOrUpdate(data: PatternSpecifiedData): Long

    @Query("DELETE FROM pattern_host_data")
    fun removeSpecifiedAll()

    @Query("DELETE FROM pattern_specified_data")
    fun removeHostAll()
}
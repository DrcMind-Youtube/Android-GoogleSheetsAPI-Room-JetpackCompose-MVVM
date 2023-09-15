package com.drcmind.stockshare.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stocks : List<StockEntity>)

    @Query("SELECT * FROM stockentity")
    fun observeStock() : Flow<List<StockEntity>>
}
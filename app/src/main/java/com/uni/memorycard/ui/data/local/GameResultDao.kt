package com.uni.memorycard.ui.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.uni.memorycard.ui.model.GameResult
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {
    @Insert
    suspend fun insert(result: GameResult)

    @Query("SELECT * FROM game_results ORDER BY date DESC")
    fun getAllResults(): Flow<List<GameResult>>

    @Query("SELECT * FROM game_results WHERE playerName = :playerName ORDER BY timeSeconds ASC")
    fun getResultsByPlayer(playerName: String): Flow<List<GameResult>>
}

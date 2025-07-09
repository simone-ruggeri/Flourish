package com.example.flourish.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flourish.data.model.SleepRating

@Dao
interface SleepRatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sleepRating: SleepRating)

    @Query("SELECT * FROM sleep_ratings WHERE date = :date AND user_id = :userId LIMIT 1")
    suspend fun getSleepRatingByDateAndUser(date: String, userId: Long): SleepRating?

    @Query("SELECT * FROM sleep_ratings WHERE user_id = :userId ORDER BY date DESC")
    suspend fun getAll(userId: Long): List<SleepRating>
}
package com.example.flourish.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flourish.data.model.MoodRating

@Dao
interface MoodRatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moodRating: MoodRating)

    @Query("SELECT * FROM mood_ratings WHERE user_id = :userId AND date BETWEEN :startDate || ' 00:00:00' AND :endDate || ' 23:59:59'")
    suspend fun getMoodRatingsForWeek(userId: Long, startDate: String, endDate: String): List<MoodRating>
}

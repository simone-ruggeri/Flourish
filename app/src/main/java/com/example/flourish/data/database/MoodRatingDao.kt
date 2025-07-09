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

    @Query("SELECT * FROM mood_ratings WHERE date = :date AND user_id = :userId LIMIT 1")
    suspend fun getMoodRatingByDateAndUser(date: String, userId: Long): MoodRating?

    @Query("SELECT * FROM mood_ratings WHERE user_id = :userId ORDER BY date DESC")
    suspend fun getAll(userId: Long): List<MoodRating>
}

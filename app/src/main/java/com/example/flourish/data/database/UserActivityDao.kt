package com.example.flourish.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flourish.data.model.UserActivity

@Dao
interface UserActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: UserActivity): Long

    @Query("SELECT * FROM user_activities WHERE userId = :userId AND date = :date")
    suspend fun getActivitiesForUserByDate(userId: Long, date: String): List<UserActivity>

    @Query("SELECT * FROM user_activities WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date")
    suspend fun getActivitiesInDateRange(userId: Long, startDate: String, endDate: String): List<UserActivity>
}
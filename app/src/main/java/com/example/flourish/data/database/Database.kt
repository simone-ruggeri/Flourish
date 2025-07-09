package com.example.flourish.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flourish.data.model.MoodRating
import com.example.flourish.data.model.SleepRating
import com.example.flourish.data.model.User
import com.example.flourish.data.model.UserActivity

@Database(
    entities = [
        User::class,
        UserActivity::class,
        SleepRating::class,
        MoodRating::class,
    ],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userActivityDao(): UserActivityDao
    abstract fun sleepRatingDao(): SleepRatingDao
    abstract fun moodRatingDao(): MoodRatingDao
}
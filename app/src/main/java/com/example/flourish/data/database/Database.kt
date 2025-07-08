package com.example.flourish.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flourish.data.model.User
import com.example.flourish.data.model.UserActivity

@Database(
    entities = [
        User::class,
        UserActivity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userActivityDao(): UserActivityDao
}
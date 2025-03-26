package com.example.flourish.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flourish.data.model.User

@Database(
    entities = [
        User::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
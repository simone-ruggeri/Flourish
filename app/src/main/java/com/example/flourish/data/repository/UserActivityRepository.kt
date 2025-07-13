package com.example.flourish.data.repository

import com.example.flourish.data.database.UserActivityDao
import com.example.flourish.data.model.UserActivity

class UserActivityRepository(private val userActivityDao: UserActivityDao) {
    suspend fun insertActivity(activity: UserActivity): Long {
        return userActivityDao.insertActivity(activity)
    }

    suspend fun getActivitiesByDate(userId: Long, date: String): List<UserActivity> {
        return userActivityDao.getActivitiesForUserByDate(userId, date)
    }

    suspend fun getActivitiesForWeek(userId: Long, startDate: String, endDate: String): List<UserActivity> {
        return userActivityDao.getActivitiesForWeek(userId, startDate, endDate)
    }

    suspend fun getWeeklyWaterDrops(userId: Long, startDate: String, endDate: String): Int {
        return userActivityDao.getWeeklyWaterDrops(userId, startDate, endDate) ?: 0
    }
}
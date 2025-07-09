package com.example.flourish.data.repository

import com.example.flourish.data.database.SleepRatingDao
import com.example.flourish.data.model.SleepRating
import java.time.LocalDate

class SleepRatingRepository(private val dao: SleepRatingDao) {

    suspend fun insert(sleepRating: SleepRating) {
        dao.insert(sleepRating)
    }

    suspend fun getSleepRatingByDateAndUser(date: String, userId: Long): SleepRating? {
        return dao.getSleepRatingByDateAndUser(date, userId)
    }

    suspend fun getAll(userId: Long): List<SleepRating> {
        return dao.getAll(userId)
    }
}

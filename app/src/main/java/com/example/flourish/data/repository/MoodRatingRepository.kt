package com.example.flourish.data.repository

import com.example.flourish.data.database.MoodRatingDao
import com.example.flourish.data.model.MoodRating
import java.time.LocalDate
import java.time.LocalDateTime

class MoodRatingRepository(private val dao: MoodRatingDao) {

    suspend fun insert(moodRating: MoodRating) {
        dao.insert(moodRating)
    }

    suspend fun getMoodRatingByDateAndUser(date: String, userId: Long): MoodRating? {
        return dao.getMoodRatingByDateAndUser(date, userId)
    }

    suspend fun getAll(userId: Long): List<MoodRating> {
        return dao.getAll(userId)
    }
}

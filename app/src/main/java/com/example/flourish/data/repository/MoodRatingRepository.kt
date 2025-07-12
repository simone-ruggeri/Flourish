package com.example.flourish.data.repository

import com.example.flourish.data.database.MoodRatingDao
import com.example.flourish.data.model.MoodRating
import java.time.LocalDate
import java.time.LocalDateTime

class MoodRatingRepository(private val dao: MoodRatingDao) {

    suspend fun insert(moodRating: MoodRating) {
        dao.insert(moodRating)
    }

    suspend fun getMoodRatingsForWeek(userId: Long, startDate: String, endDate: String): List<MoodRating> {
        return dao.getMoodRatingsForWeek(userId, startDate, endDate)
    }
}

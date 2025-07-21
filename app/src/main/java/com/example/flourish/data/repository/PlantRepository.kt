package com.example.flourish.data.repository

import com.example.flourish.data.database.PlantDao
import com.example.flourish.data.model.Plant

class PlantRepository(private val plantDao: PlantDao) {

    suspend fun getPlantByUserId(userId: Long): Plant? {
        return plantDao.getPlantByUserId(userId)
    }

    suspend fun insertOrUpdatePlant(userId: Long, stage: Int, health: String) {
        val existingPlant = plantDao.getPlantByUserId(userId)
        if (existingPlant != null) {
            val updated = existingPlant.copy(stage = stage, health = health)
            plantDao.updatePlant(updated)
        } else {
            val newPlant = Plant(userId = userId, stage = stage, health = health)
            plantDao.insertPlant(newPlant)
        }
    }
}

package com.example.flourish.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.flourish.data.model.Plant

@Dao
interface PlantDao {

    @Query("SELECT * FROM plants WHERE user_id = :userId LIMIT 1")
    suspend fun getPlantByUserId(userId: Long): Plant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: Plant): Long

    @Update
    suspend fun updatePlant(plant: Plant)

    @Delete
    suspend fun deletePlant(plant: Plant)
}

package com.example.flourish.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_activities",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // elimina le attività se l'utente viene eliminato
        )
    ],
    indices = [Index(value = ["userId"])] // migliora le performance nelle query
)
data class UserActivity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val userId: Long,
    val date: String, // formato consigliato: "yyyy-MM-dd"
    val activityName: String,
    val iconRes: Int,
    val minutes: Int,
    val waterDrops: Int
)
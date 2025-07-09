package com.example.flourish.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sleep_ratings",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_id"])]
)
data class SleepRating(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "rating") val rating: String,
    @ColumnInfo(name = "user_id") val userId: Long
)



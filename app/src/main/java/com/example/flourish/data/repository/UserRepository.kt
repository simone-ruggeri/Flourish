package com.example.flourish.data.repository

import com.example.flourish.data.database.UserDao
import com.example.flourish.data.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun registerUser(user: User): Result<Long> {
        return try {
            // Verifica se esiste già un utente con la stessa email
            val existingUser = userDao.getUserByEmail(user.email)
            if (existingUser != null) {
                // Se esiste, restituisci un errore
                return Result.failure(Exception("Email already in use"))
            }

            // Altrimenti, registra il nuovo utente
            val userId = userDao.insertUser(user)
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    suspend fun loginUser(email: String, password: String): Result<User?> {
        return try {
            val user = userDao.loginUser(email, password)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfileImage(userId: Long, imagePath: String) {
        userDao.updateProfileImage(userId, imagePath)
    }
}
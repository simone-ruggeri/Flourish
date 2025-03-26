package com.example.flourish

import androidx.room.Room
import com.example.flourish.data.database.AppDatabase
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.UserRepository
import com.example.flourish.viewmodel.LoginViewModel
import com.example.flourish.viewmodel.SignupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Database
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "flourish"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // DAO
    single { get<AppDatabase>().userDao() }

    // Repository
    single { UserRepository(get()) }

    //UserPreferences
    single { UserPreferences(get()) }

    //ViewModel
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SignupViewModel(get())}
}
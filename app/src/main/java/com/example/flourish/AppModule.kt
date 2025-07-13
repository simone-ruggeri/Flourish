package com.example.flourish

import androidx.room.Room
import com.example.flourish.data.database.AppDatabase
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.MoodRatingRepository
import com.example.flourish.data.repository.SleepRatingRepository
import com.example.flourish.data.repository.UserActivityRepository
import com.example.flourish.data.repository.UserRepository
import com.example.flourish.viewmodel.ActivityDialogViewModel
import com.example.flourish.viewmodel.ChartViewModel
import com.example.flourish.viewmodel.HomepageViewModel
import com.example.flourish.viewmodel.LoginViewModel
import com.example.flourish.viewmodel.MoodRatingViewModel
import com.example.flourish.viewmodel.ProfileViewModel
import com.example.flourish.viewmodel.SignupViewModel
import com.example.flourish.viewmodel.SleepRatingViewModel
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
    single { get<AppDatabase>().userActivityDao() }
    single { get<AppDatabase>().sleepRatingDao() }
    single { get<AppDatabase>().moodRatingDao()}

    // Repository
    single { UserRepository(get()) }
    single { UserActivityRepository(get()) }
    single { SleepRatingRepository(get()) }
    single { MoodRatingRepository(get()) }

    //UserPreferences
    single { UserPreferences(get()) }

    //ViewModel
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SignupViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ActivityDialogViewModel(get(), get()) }
    viewModel { SleepRatingViewModel(get(), get()) }
    viewModel { MoodRatingViewModel(get(), get()) }
    viewModel { ChartViewModel(get(), get(), get(), get()) }
    viewModel { HomepageViewModel(get(), get()) }
}
package com.example.flourish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.data.repository.UserRepository
import com.example.flourish.ui.components.CustomBottomAppBar.BottomAppBarHandler
import com.example.flourish.ui.navigation.NavGraph
import com.example.flourish.ui.theme.FlourishTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val userPreferences: UserPreferences by inject()
    private val userRepository: UserRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlourishTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar ={ BottomAppBarHandler(navController = navController) }
                    ) { contentPadding ->
                        NavGraph(
                            navController = navController,
                            userPreferences = userPreferences,
                            userRepository = userRepository,
                            modifier = Modifier.padding(contentPadding)
                        )
                    }
                }
            }
        }
    }
}
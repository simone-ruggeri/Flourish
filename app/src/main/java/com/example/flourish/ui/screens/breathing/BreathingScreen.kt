package com.example.flourish.ui.screens.breathing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.flourish.viewmodel.BreathingViewModel

@Composable
fun BreathingScreen(
    viewModel: BreathingViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val context = LocalContext.current

    // Initialize TextToSpeech once
    LaunchedEffect(Unit) {
        viewModel.initTts(context)
    }

    // Load Lottie composition based on current phase resource
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(uiState.lottieResId)
    )

    // Create an animatable to control playback
    val animatable = rememberLottieAnimatable()

    // Restart the animation only when speaking starts or composition changes
    LaunchedEffect(composition, isSpeaking) {
        if (isSpeaking && composition != null) {
            animatable.snapTo(progress = 0f)
            animatable.animate(
                composition = composition,
                iterations = 1,
                speed = 1f
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Full-screen Lottie animation
        LottieAnimation(
            composition = composition,
            progress = { animatable.progress },
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Back arrow at top-left (above animation)
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Indietro",
                tint = Color.White
            )
        }

        // Instruction text
        Text(
            text = uiState.instruction,
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        // Play/Stop buttons
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!isSpeaking) {
                Button(onClick = { viewModel.startBreathing() }) {
                    Text("Play")
                }
            } else {
                Button(onClick = { viewModel.stopBreathing() }) {
                    Text("Stop")
                }
            }
        }
    }
}
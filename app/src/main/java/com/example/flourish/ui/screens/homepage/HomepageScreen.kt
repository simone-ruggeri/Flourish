package com.example.flourish.ui.screens.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.flourish.R
import com.example.flourish.viewmodel.HomepageViewModel
import kotlinx.coroutines.delay

val plantDrawables = mapOf(
    Pair(0, "healthy") to R.drawable.plant_v_1_healthy,
    Pair(0, "struggling") to R.drawable.plant_v_1_struggling,
    Pair(0, "wilted") to R.drawable.plant_v_1_wilted,

    Pair(1, "healthy") to R.drawable.plant_v_2_healthy,
    Pair(1, "struggling") to R.drawable.plant_v_2_struggling,
    Pair(1, "wilted") to R.drawable.plant_v_2_wilted,

    Pair(2, "healthy") to R.drawable.plant_v_3_healthy,
    Pair(2, "struggling") to R.drawable.plant_v_3_struggling,
    Pair(2, "wilted") to R.drawable.plant_v_3_wilted,

    Pair(3, "healthy") to R.drawable.plant_v_4_healthy,
    Pair(3, "struggling") to R.drawable.plant_v_4_struggling,
    Pair(3, "wilted") to R.drawable.plant_v_4_wilted,

    Pair(4, "healthy") to R.drawable.plant_v_5_healthy,
    Pair(4, "struggling") to R.drawable.plant_v_5_struggling,
    Pair(4, "wilted") to R.drawable.plant_v_5_wilted,
)

val plantSize = 470.dp

val transitionAnimations = mapOf(
    0 to R.raw.transition_v1_healthy_to_v2_healthy,
    1 to R.raw.transition_v2_healthy_to_v3_healthy,
    2 to R.raw.transition_v3_healthy_to_v4_healthy,
    3 to R.raw.transition_v4_healthy_to_v5_healthy
)

@Composable
fun HomepageScreen(
    navController: NavHostController,
    viewModel: HomepageViewModel
) {
    val weeklyWaterDrops by viewModel.weeklyWaterDrops.collectAsState()
    val plantStatus by viewModel.plantStatus.collectAsState()

    val plantStage by viewModel.plantStage.collectAsState()
    val plantHealth by viewModel.plantHealth.collectAsState()
    val showTransition by viewModel.showTransition.collectAsState()

    val plantDrawable = plantDrawables[plantStage to plantHealth] ?: R.drawable.plant_v_1_healthy
    val transitionLottieFile = transitionAnimations[plantStage]

    LaunchedEffect(Unit) {
        viewModel.setPlantStage(0)
        viewModel.setPlantHealth("healthy")
        delay(2000)
        viewModel.setPlantHealth("struggling")
        delay(2000)
        viewModel.setPlantHealth("wilted")
        delay(2000)
        viewModel.setShowTransition(true)
        delay(2000)
        viewModel.setShowTransition(false)

        viewModel.setPlantStage(1)
        viewModel.setPlantHealth("healthy")
        delay(2000)
        viewModel.setPlantHealth("struggling")
        delay(2000)
        viewModel.setPlantHealth("wilted")
        delay(2000)
        viewModel.setShowTransition(true)
        delay(2000)
        viewModel.setShowTransition(false)

        viewModel.setPlantStage(2)
        viewModel.setPlantHealth("healthy")
        delay(2000)
        viewModel.setPlantHealth("struggling")
        delay(2000)
        viewModel.setPlantHealth("wilted")
        delay(2000)
        viewModel.setShowTransition(true)
        delay(2000)
        viewModel.setShowTransition(false)

        viewModel.setPlantStage(3)
        viewModel.setPlantHealth("healthy")
        delay(2000)
        viewModel.setPlantHealth("struggling")
        delay(2000)
        viewModel.setPlantHealth("wilted")
        delay(2000)
        viewModel.setShowTransition(true)
        delay(2000)
        viewModel.setShowTransition(false)

        viewModel.setPlantStage(4)
        viewModel.setPlantHealth("healthy")
        delay(2000)
        viewModel.setPlantHealth("struggling")
        delay(2000)
        viewModel.setPlantHealth("wilted")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Cultivate your well-being every day by taking care of yourself",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(476.dp), // punto fisso dove sta la pianta
                contentAlignment = Alignment.BottomCenter
            ) {
                if (showTransition && transitionLottieFile != null) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(transitionLottieFile))
                    LottieAnimation(
                        composition = composition,
                        iterations = 1,
                        modifier = Modifier.height(plantSize)
                    )
                } else {
                    Image(
                        painter = painterResource(id = plantDrawable),
                        contentDescription = "Current plant stage",
                        modifier = Modifier
                            .height(plantSize)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = plantStatus,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.week_calendar),
                            contentDescription = "week-calendar-icon",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Drops earned this week",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = weeklyWaterDrops.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.water_drop),
                            contentDescription = "water-drop-icon",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your plant needs at least 40 drops of water per week to survive, but 50 drops will help it grow to the next stage. Complete tasks to earn drops!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Bonus: Log your activities every day for a full week to earn 5 extra drops! Stay consistent and keep your plant thriving!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

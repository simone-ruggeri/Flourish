package com.example.flourish.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {
    // Contenitore che occupa tutta la schermata
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFf7f4f2))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Centra orizzontalmente
            verticalArrangement = Arrangement.Center, // Centra verticalmente
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = Color(0xFF4F3422), // Colore dell'indicatore (puoi personalizzarlo)
                modifier = Modifier.size(80.dp) // Imposta la dimensione del CircularProgressIndicator
            )
            Spacer(modifier = Modifier.height(16.dp)) // Distanza tra il CircularProgressIndicator e la scritta
            Text(
                text = "Loading...", // Testo da visualizzare
                style = MaterialTheme.typography.bodyLarge, // Puoi personalizzare lo stile del testo
                color = Color(0xFF4F3422) // Colore del testo, generalmente bianco o nero
            )
        }
    }
}
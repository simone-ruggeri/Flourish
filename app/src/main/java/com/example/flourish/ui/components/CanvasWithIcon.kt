package com.example.flourish.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.flourish.R

@Composable
fun CanvasWithIcon() {
    val backgroundColor = MaterialTheme.colorScheme.onBackground
    Box(contentAlignment = Alignment.Center) {
        // Canvas con sfondo curvo
        Canvas(
            modifier = Modifier
                .fillMaxWidth() // Riempi tutta la larghezza disponibile
                .height(180.dp) // Altezza della sezione verde
        ) {
            // Creazione di un oggetto Path per tracciare linee e curve
            val path = Path().apply {

                // Inizio del percorso (in basso a sinistra)
                moveTo(0f, size.height * 0.8f)

                // Disegno della curva con quadraticTo
                quadraticTo(
                    // Punto di controllo della curva (al centro)
                    size.width / 2, size.height * 1.2f,

                    // Punto finale della curva (in basso a destra)
                    size.width, size.height * 0.8f
                )

                // Linea fino all'angolo in alto a destra
                lineTo(size.width, 0f)

                // Linea fino all'angolo in alto a sinistra
                lineTo(0f, 0f)

                // Chiudi il percorso per riempire l'area
                close()
            }

            // Disegna il percorso sul canvas con il colore verde
            drawPath(path, color = backgroundColor)
        }

        // Icona posizionata sopra la curva
        Icon(
            painter = painterResource(id = R.drawable.login_plant),
            contentDescription = "Plant",
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.TopCenter)
                .offset(y = 80.dp),
            tint = Color.White
        )
    }
}
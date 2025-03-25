package com.example.flourish.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.flourish.ui.components.CanvasWithIcon

@Composable
fun LoginScreen(
    navController: NavHostController
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFf7f4f2))
            .clickable(
                // Rimuove il focus (e nasconde la tastiera) quando si clicca all'esterno
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() }
    ) {
        CanvasWithIcon()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Aggiungi il testo sotto la CanvasWithIcon
            Text(
                text = "Welcome Back, please login to your account",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF4F3422)
            )
            Spacer(modifier = Modifier.height(48.dp))

            // Email Input
            TextField(
                value = "",
                onValueChange = { "" },
                label = {
                    Text(
                        text = "Email",
                        color = Color(0xFF4F3422)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
                //colors =
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            TextField(
                value = "",
                onValueChange = { "" },
                label = {
                    Text(
                        text = "Password",
                        color = Color(0xFF4F3422)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
                //colors =
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F3422),
                    contentColor = Color.White
                )
            ){
                Text(text = "Login")
            }
            Spacer(modifier = Modifier.height(96.dp))

            TextButton(
                onClick = { /*TODO*/ }
            ) {
                val annotatedString = buildAnnotatedString {
                    // Prima parte del testo: "Don't have an account?"
                    withStyle(style = SpanStyle(color = Color(0xFF4F3422))) {
                        append("Don't have an account? ")
                    }
                    // Seconda parte del testo: "Sign Up" con colore diverso e come link
                    withStyle(style = SpanStyle(color = Color(0xFFED7E1C))) {
                        append("Sign Up")
                    }
                }
                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
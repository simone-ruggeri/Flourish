package com.example.flourish.ui.screens.signup

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.flourish.ui.components.ErrorMessage
import com.example.flourish.ui.navigation.NavigationRoute
import com.example.flourish.viewmodel.SignupViewModel

@Composable
fun SignupScreen(
    navController: NavHostController,
    signupViewModel: SignupViewModel
) {
    val uiState by signupViewModel.registrationUiState.collectAsState()
    val registrationState by signupViewModel.registrationState.collectAsState()

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
            Text(
                text = "Create a new account",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF4F3422)
            )
            Spacer(modifier = Modifier.height(48.dp))

            // First name Input
            uiState.firstNameError?.let { ErrorMessage(text = it) }
            TextField(
                value = uiState.firstName,
                onValueChange = { signupViewModel.onFirstNameChanged(it) },
                label = {
                    Text(
                        text = "First Name",
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

            // Last name Input
            uiState.lastNameError?.let { ErrorMessage(text = it) }
            TextField(
                value = uiState.lastName,
                onValueChange = { signupViewModel.onLastNameChanged(it) },
                label = {
                    Text(
                        text = "Last Name",
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

            // Email Input
            uiState.emailError?.let { ErrorMessage(text = it) }
            TextField(
                value = uiState.email,
                onValueChange = { signupViewModel.onEmailChanged(it) },
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
            uiState.passwordError?.let { ErrorMessage(text = it) }
            TextField(
                value = uiState.password,
                onValueChange = { signupViewModel.onPasswordChanged(it) },
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
                onClick = {
                    signupViewModel.registerUser()
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F3422),
                    contentColor = Color.White
                )
            ){
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(
                        text = "Sign up",
                        color = Color.White
                    )
                }
            }

            // Error Message
            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(64.dp))

            TextButton(
                onClick = { navController.navigate(NavigationRoute.Login.route) }
            ) {
                val annotatedString = buildAnnotatedString {
                    // Prima parte del testo: "Don't have an account?"
                    withStyle(style = SpanStyle(color = Color(0xFF4F3422))) {
                        append("Already have an account? ")
                    }
                    // Seconda parte del testo: "Sign Up" con colore diverso e come link
                    withStyle(style = SpanStyle(color = Color(0xFFED7E1C))) {
                        append("Login")
                    }
                }
                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        LaunchedEffect(registrationState) {
            registrationState?.let {
                if (it.isSuccess && it.getOrNull() != null) {
                    // Esegui la navigazione se il login è riuscito
                    navController.navigate(NavigationRoute.Login.route) {
                        popUpTo(NavigationRoute.SignUp.route) { inclusive = true }
                    }
                }
            }
        }
    }
}
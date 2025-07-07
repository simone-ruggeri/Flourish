package com.example.flourish.ui.screens.profile

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.flourish.ui.components.Semicircle
import com.example.flourish.R
import com.example.flourish.ui.components.rememberCameraLauncher
import com.example.flourish.ui.navigation.NavigationRoute
import com.example.flourish.utils.loadBitmapFromUri
import com.example.flourish.utils.rememberPermission
import com.example.flourish.utils.saveImageToInternalStorage
import com.example.flourish.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel
) {
    val logoutState by profileViewModel.logoutEvent.collectAsState()
    val user by profileViewModel.profileState.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadUser()
    }

    // Se logoutState diventa true, esegui la navigazione alla schermata di login
    LaunchedEffect(logoutState) {
        if (logoutState) {
            navController.navigate(NavigationRoute.Login.route) {
                // Rimuove la schermata di profilo dallo stack
                popUpTo(NavigationRoute.Profile.route) { inclusive = true }
            }
        }
    }

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val cameraLauncher = rememberCameraLauncher { imageUri ->
        val bitmap = loadBitmapFromUri(context, imageUri)
        bitmap?.let { bmp ->
            val timestamp = System.currentTimeMillis()
            val imagePath = saveImageToInternalStorage(context, bmp, "profile_image_${user.id}_$timestamp.jpg")
            imagePath?.let { path ->
                profileViewModel.updateProfileImage(path) // Aggiorna il database
            }
        }
    }

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = loadBitmapFromUri(context, it)
            bitmap?.let { bmp ->
                val timestamp = System.currentTimeMillis()
                val imagePath = saveImageToInternalStorage(context, bmp, "profile_image_${user.id}_$timestamp.jpg")
                imagePath?.let { path ->
                    profileViewModel.updateProfileImage(path) // Aggiorna il database
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Semicircle(showIcon = false)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 110.dp), // Sposta in basso rispetto alla curva
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = if (user.profileImageUri.isNotBlank()) {
                    rememberAsyncImagePainter(user.profileImageUri)
                } else {
                    painterResource(id = R.drawable.profile_picture_default)
                },
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 15.dp)
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.profile_edit),
                    contentDescription = "Edit Profile Picture",
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(72.dp))
            InfoCard(
                leadingIcon = R.drawable.bottom_app_bar_profile,
                title = user.name
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoCard(
                leadingIcon = R.drawable.profile_email,
                title = user.email
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { profileViewModel.logout() },
                modifier = Modifier.wrapContentWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_logout),
                        contentDescription = "logout-icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = "Change Profile Image",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                text = {
                    Column {
                        TextButton(onClick = {
                            if (cameraPermission.status.isGranted) {
                                cameraLauncher.captureImage()
                            } else {
                                cameraPermission.launchPermissionRequest()
                            }
                            showDialog = false
                        }) {
                            Text(
                                text = "Take a Photo",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        TextButton(onClick = {
                            pickImageLauncher.launch("image/*")
                            showDialog = false
                        }) {
                            Text(
                                text = "Choose from Gallery",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        TextButton(onClick = {
                            profileViewModel.removeProfileImage()
                            showDialog = false
                        }) {
                            Text(
                                text = "Remove Profile Picture",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                confirmButton = { },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(
                            text = "Cancel",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.background
            )
        }
    }
}
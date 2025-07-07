package com.example.flourish.utils

import android.net.Uri

interface CameraLauncher {
    val capturedImageUri: Uri
    fun captureImage()
}
package com.example.flourish.viewmodel

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flourish.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

data class BreathingUiState(
    val instruction: String = "Premi per iniziare",
    val lottieResId: Int = R.raw.animation_inhale
)

class BreathingViewModel : ViewModel() {
    private var tts: TextToSpeech? = null

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking

    val uiState = MutableStateFlow(BreathingUiState())

    // Fasi della respirazione con durata personalizzabile
    private val inhaleDuration = 3800L
    private val holdDuration = 3800L
    private val exhaleDuration = 3800L

    private val breathingPhases = listOf(
        Phase("Inhale", R.raw.animation_inhale, inhaleDuration),
        Phase("Hold it", R.raw.animation_hold, holdDuration),
        Phase("Exhale", R.raw.animation_exhale, exhaleDuration)
    )

    data class Phase(val instruction: String, val animationRes: Int, val durationMs: Long)

    fun initTts(context: Context) {
        if (tts != null) return
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
                tts?.setPitch(0.8f)
                tts?.setSpeechRate(0.8f)
            }
        }
    }

    fun startBreathing() {
        if (_isSpeaking.value) return
        _isSpeaking.value = true

        viewModelScope.launch(Dispatchers.Default) {
            while (_isSpeaking.value) {
                for (phase in breathingPhases) {
                    if (!_isSpeaking.value) break

                    uiState.value = BreathingUiState(
                        instruction = phase.instruction,
                        lottieResId = phase.animationRes
                    )

                    tts?.speak(phase.instruction, TextToSpeech.QUEUE_FLUSH, null, null)
                    delay(phase.durationMs)
                }
            }

            // Al termine o stop, resetta la UI
            resetUi()
        }
    }

    fun stopBreathing() {
        _isSpeaking.value = false
        tts?.stop()
        resetUi()
    }

    private fun resetUi() {
        uiState.value = BreathingUiState(
            instruction = "Premi per iniziare",
            lottieResId = R.raw.animation_inhale
        )
    }

    override fun onCleared() {
        super.onCleared()
        tts?.shutdown()
    }
}
package dev.leonardom.evaluacionupax.core.util

sealed class ProgressBarState{
    object Loading: ProgressBarState()
    object Idle: ProgressBarState()
}

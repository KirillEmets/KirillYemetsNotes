package com.kirillemets.kirillyemetsnotes.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.mySwipeable(noteId: Long, onSwipe: () -> Unit, setClickable: (Boolean) -> Unit): Modifier = composed {
    val offsetXpx = remember(noteId) { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val draggableState = rememberDraggableState(onDelta = {
        scope.launch {
            offsetXpx.snapTo(max(offsetXpx.value + it, 0f))
        }
    })
    val metrics = LocalContext.current.resources.displayMetrics
    val offsetXdp = offsetXpx.value  / metrics.density

    Modifier
        .absoluteOffset(x = offsetXdp.dp)
        .draggable(draggableState, orientation = Orientation.Horizontal, onDragStopped = { acceleration ->
            if (offsetXdp > 50 && abs(acceleration) > 100) {
                setClickable(false)
                launch {
                    offsetXpx.animateTo(metrics.widthPixels.toFloat())
                    onSwipe()
                }
            }
            launch {
                if (!offsetXpx.isRunning)
                    offsetXpx.animateTo(0f)
            }
        }, onDragStarted = {
            offsetXpx.snapTo(max(offsetXpx.value + it.x, 0f))
        })
}
package com.example.tiendahotwheels.views

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun TiltImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val rotationX = remember { Animatable(0f) }
    val rotationY = remember { Animatable(0f) }

    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .graphicsLayer {
                this.rotationX = rotationX.value
                this.rotationY = rotationY.value
                cameraDistance = 32f * density
            }
            .pointerInput(Unit) {
                coroutineScope {
                    detectDragGestures(
                        onDrag = { _, drag ->

                            launch {
                                rotationX.animateTo(
                                    rotationX.value - drag.y * 0.25f,
                                    tween(1)
                                )
                            }

                            launch {
                                rotationY.animateTo(
                                    rotationY.value + drag.x * 0.25f,
                                    tween(1)
                                )
                            }
                        },

                        onDragEnd = {
                            launch { rotationX.animateTo(0f, tween(500)) }
                            launch { rotationY.animateTo(0f, tween(500)) }
                        }
                    )
                }
            }
    )
}

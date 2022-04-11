package com.zgsbrgr.compose.anim.ui

import android.view.animation.BounceInterpolator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.zgsbrgr.compose.anim.DoubleSide
import com.zgsbrgr.compose.anim.FLIPTYPE
import com.zgsbrgr.compose.anim.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

@Composable
fun Wordle() {

    val text = "WELCOME"
    val letterCount = text.count()
    val padding = 24.dp
    var enabled by remember { mutableStateOf(true) }

    val transition = updateTransition(targetState = enabled, label = "")

    val started = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(started) {
        launch {
            delay(300)
            enabled = !enabled
        }
    }


    Row(
        Modifier
            .fillMaxWidth()
            .padding(padding, 100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(letterCount) {
                count ->
            val firstDelay = 250
            val firstDuration = 500
            val secondDelay = 100
            val secondDuration = 500
            val previousDelay = (letterCount - 1) * firstDelay + firstDuration

            val rotationX by transition.animateFloat(
                transitionSpec = {
                    tween(
                        delayMillis = count * firstDelay,
                        durationMillis = firstDuration
                    )
                },
                label = ""
            ) { if (it) 0f else 180f }

            val translationY by transition.animateFloat(
                transitionSpec = {
                    tween(
                        delayMillis = previousDelay + count * secondDelay,
                        durationMillis = secondDuration,
                        easing = { BounceInterpolator().getInterpolation(it) }
                    )
                },
                label = ""
            ) { if (it) 0f else 180f }

            DoubleSide(
                rotationX = rotationX,
                translationY = -100 * sin(translationY * Math.PI.toFloat()/180f),
                flipType = FLIPTYPE.HORIZONTAL,
                cameraDistance = 100f,
                front = {
                    MyLetterCard(
                        text[count].toString(),
                        colorResource(id = R.color.color_blue),
                        colorResource(id = R.color.color_blue)
                    )
                },
                back = {
                    MyLetterCard(
                        text[count].toString(),
                        colorResource(id = R.color.color_coral),
                        colorResource(id = R.color.color_blue)
                    )
                }
            )
        }

    }

}

@Composable
fun MyLetterCard(text: String, color: Color, border: Color) {
    val roundedDegree = RoundedCornerShape(2.dp)
    Box(
        Modifier
            .clip(roundedDegree)
            .background(color)
            .size(50.dp, 50.dp)
            .border(2.dp, border, shape = roundedDegree),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}
package com.zgsbrgr.compose.anim

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.toUpperCase
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.zgsbrgr.compose.anim.data.sports
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


enum class MenuItemPosition {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT
}


@Composable
fun MenuItem(menuItemPosition: MenuItemPosition, label: String, modifier: Modifier, onMenuItemClicked: (MenuItemPosition) -> Unit) {


    var isPressed by remember {
        mutableStateOf(false)
    }

    CompositionLocalProvider(LocalIndication provides rememberRipple(color = Color.Black)) {
        Box(
            modifier = modifier
                .background(
                    color = colorResource(id = if (!isPressed) R.color.color_blue else R.color.color_dark_blue),
                    RectangleShape
                )
                .clickable(
                    onClick = {
                        isPressed = !isPressed
                        onMenuItemClicked(menuItemPosition)
                    }
                ),

            ) {

            Text(
                label.uppercase(),
                modifier = Modifier.align(Alignment.Center), color = Color.White, textAlign = TextAlign.Center, style = TextStyle(letterSpacing = 10.sp, fontSize = 16.sp, fontWeight = FontWeight.Light)
            )

        }
    }


}

enum class ViewSate {
    Open,
    Closed,
    Navigate
}

@Composable
fun MainScreen(onNavigation:(String) -> Unit) {

    val (screenWidthInPx, screenHeightInPx) = with(LocalConfiguration.current) {
        with(LocalDensity.current) {
            screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx()
        }
    }

    val (boxWidth, boxHeight) = with(LocalDensity.current) {
        (screenWidthInPx/2).toDp() to (screenHeightInPx/2).toDp()
    }

    val (leftOffsetX, rightOffsetX) = remember {
        Animatable(-screenWidthInPx/2) to Animatable(screenWidthInPx)
    }

    val (topOffsetY, bottomOffsetY) = remember {
        Animatable(-screenHeightInPx/2) to Animatable(screenHeightInPx)
    }

    val buttonOffsetY = remember {
        Animatable(0f)
    }

    val buttonSizeInPx = with(LocalDensity.current) {
        (65/2).dp.toPx()
    }

    val headerHeightInPx = with(LocalDensity.current) {
        (150/2).dp.toPx()
    }



    var menuState by remember {
        mutableStateOf(ViewSate.Closed)
    }


    val backgroundColor: Color by animateColorAsState(
        Color.White
    )

    val composableScope = rememberCoroutineScope()



    fun onButtonClick(navigationRoute: String?) {
        composableScope.launch {

            launch {
                leftOffsetX.animateTo(
                    targetValue = if(menuState == ViewSate.Closed) -2f else -screenWidthInPx/2,
                    animationSpec = tween(
                        300
                    )
                )



            }
            launch {
                topOffsetY.animateTo(
                    targetValue = if(menuState == ViewSate.Closed) -2f else -screenHeightInPx/2,
                    animationSpec = tween(300)
                )
            }
            launch {
                rightOffsetX.animateTo(
                    targetValue = if(menuState == ViewSate.Closed) screenWidthInPx/2+2f else screenWidthInPx,
                    animationSpec = tween(
                        300
                    )
                )
            }
            launch {
                bottomOffsetY.animateTo(
                    targetValue = if(menuState == ViewSate.Closed) screenHeightInPx/2+2f else screenHeightInPx,
                    animationSpec = tween(300)
                )

            }
            launch {

                when(menuState) {
                    ViewSate.Closed -> menuState = ViewSate.Open
                    else -> {
                        delay(300)
                        navigationRoute?.let {
                            onNavigation(it)

                        }?: kotlin.run {
                            onNavigation("welcome")

                        }

                    }
                }
            }

        }

    }


    fun onMenuItemClick(categoryId: Int) {
        composableScope.launch {
            delay(200)
            launch {
                buttonOffsetY.animateTo(
                    targetValue = -(screenHeightInPx/2 + buttonSizeInPx) + headerHeightInPx + buttonSizeInPx,
                    tween(250)
                )

            }

            launch {
                onButtonClick("card/${categoryId}")
            }

        }

    }


    if(menuState == ViewSate.Closed) {
        LaunchedEffect(key1 = menuState ) {
            delay(200)
            onButtonClick(null)
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {



        MenuItem(menuItemPosition = MenuItemPosition.TOP_LEFT, sports[0].name, modifier = Modifier
            .offset {
                IntOffset(
                    leftOffsetX.value.toInt(),
                    topOffsetY.value.toInt()
                )
            }
            .size(boxWidth, boxHeight),
            onMenuItemClicked = {
                onMenuItemClick(sports[0].categoryId)
            }
        )



        MenuItem(menuItemPosition = MenuItemPosition.TOP_RIGHT, sports[1].name, modifier = Modifier
            .offset {
                IntOffset(
                    rightOffsetX.value.toInt(),
                    topOffsetY.value.toInt()
                )
            }
            .size(boxWidth, boxHeight),
            onMenuItemClicked = {
                onMenuItemClick(sports[1].categoryId)
            }

        )

        MenuItem(menuItemPosition = MenuItemPosition.BOTTOM_LEFT, sports[2].name, modifier = Modifier
            .offset {
                IntOffset(
                    leftOffsetX.value.toInt(),
                    bottomOffsetY.value.toInt()
                )
            }
            .size(boxWidth, boxHeight),
            onMenuItemClicked = {
                onMenuItemClick(sports[2].categoryId)
            }
        )


        MenuItem(menuItemPosition = MenuItemPosition.BOTTOM_RIGHT, sports[3].name, modifier = Modifier
            .offset {
                IntOffset(
                    rightOffsetX.value.toInt(),
                    bottomOffsetY.value.toInt()
                )
            }
            .size(boxWidth, boxHeight),
            onMenuItemClicked = {
                onMenuItemClick(sports[3].categoryId)
            }

        )

        MenuButton(
            modifier = Modifier
                .graphicsLayer { translationY = buttonOffsetY.value }
                .align(Alignment.Center),
            menuState = menuState,
            onButtonClick = {
                onButtonClick(null)
            }
        )


    }

}




@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(onNavigation = {})
}


@Preview
@Composable
fun MenuItemPreview() {

    //MenuItem(menuItemPosition = MenuItemPosition.TOP_LEFT, width = 200f, height = 200f, 0f, Modifier.background(color = Color.Blue, RoundedCornerShape(5.dp)))

}
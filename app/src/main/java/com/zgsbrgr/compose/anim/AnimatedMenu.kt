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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


enum class MenuItemPosition {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT
}


@Composable
fun MenuItem(menuItemPosition: MenuItemPosition, modifier: Modifier, onMenuItemClicked: (MenuItemPosition) -> Unit) {


    var isPressed by remember {
        mutableStateOf(false)
    }

    CompositionLocalProvider(LocalIndication provides rememberRipple(color = Color.Black)) {
        Box(
            modifier = modifier
                .background(color = colorResource(id = R.color.color_blue), RectangleShape)
                .clickable(
                    onClick = {
                        isPressed = !isPressed
                        onMenuItemClicked(menuItemPosition)
                    }
                ),

            ) {

            Text(
                when(menuItemPosition) {
                    MenuItemPosition.TOP_LEFT -> "euro".uppercase()
                    MenuItemPosition.TOP_RIGHT -> "credit".uppercase()
                    MenuItemPosition.BOTTOM_LEFT -> "exchange".uppercase()
                    MenuItemPosition.BOTTOM_RIGHT -> "access".uppercase()
                },
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
    Log.d("AnimatedMenu", "MainScreen called")

    val (screenWidthInPx, screenHeightInPx) = with(LocalConfiguration.current) {
        with(LocalDensity.current) {
            screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx()
        }
    }

    val (boxWidth, boxHeight) = with(LocalDensity.current) {
        (screenWidthInPx/2).toDp() to (screenHeightInPx/2).toDp()
    }


    val leftOffsetX = remember {
        Animatable(-screenWidthInPx/2)
    }
    val rightOffsetX = remember {
        Animatable(screenWidthInPx)
    }
    val topOffsetY = remember {
       Animatable(-screenHeightInPx/2)
    }
    val bottomOffsetY = remember {
        Animatable(screenHeightInPx)
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
                            Log.d("AnimatedMenu", "onMenuItemClick")
                            onNavigation(it)

                        }?: kotlin.run {
                            onNavigation("welcome")

                        }

                    }
                }
            }

        }

    }




    var menuItemName by rememberSaveable {
        mutableStateOf("")
    }

    var menuIcon by rememberSaveable {
        mutableStateOf(R.drawable.credit_card_off_white_24dp)
    }

    fun onMenuItemClick() {
        composableScope.launch {
            launch {
                buttonOffsetY.animateTo(
                    targetValue = -(screenHeightInPx/2 + buttonSizeInPx) + headerHeightInPx + buttonSizeInPx,
                    tween(250)
                )

            }

            launch {
                onButtonClick("card")
            }

        }

    }


    LaunchedEffect(menuState) {
        if(menuState == ViewSate.Closed) {
            delay(200)
            onButtonClick(null)
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {



        MenuItem(menuItemPosition = MenuItemPosition.TOP_LEFT, modifier = Modifier
            .offset {
                IntOffset(
                    leftOffsetX.value.toInt(),
                    topOffsetY.value.toInt()
                )
            }
            .size(boxWidth, boxHeight),
            onMenuItemClicked = {
                menuItemName = "Euro"
                menuIcon = R.drawable.euro_symbol_white_24dp
                onMenuItemClick()
            }
        )



        MenuItem(menuItemPosition = MenuItemPosition.TOP_RIGHT, modifier = Modifier
            .offset {
                IntOffset(
                    rightOffsetX.value.toInt(),
                    topOffsetY.value.toInt()
                )
            }
            .size(boxWidth, boxHeight),
            onMenuItemClicked = {
                menuItemName = "Credit"
                menuIcon = R.drawable.credit_card_off_white_24dp
                onMenuItemClick()
            }

        )

        MenuItem(menuItemPosition = MenuItemPosition.BOTTOM_LEFT, modifier = Modifier
            .offset {
                IntOffset(
                    leftOffsetX.value.toInt(),
                    bottomOffsetY.value.toInt()
                )
            }
            .size(boxWidth, boxHeight),
            onMenuItemClicked = {
                menuItemName = "Exchange"
                menuIcon = R.drawable.currency_exchange_white_24dp
                onMenuItemClick()
            }
        )


        MenuItem(menuItemPosition = MenuItemPosition.BOTTOM_RIGHT, modifier = Modifier
            .offset {
                IntOffset(
                    rightOffsetX.value.toInt(),
                    bottomOffsetY.value.toInt()
                )
            }
            .size(boxWidth, boxHeight),
            onMenuItemClicked = {
                menuItemName = "Access"
                menuIcon = R.drawable.switch_access_shortcut_add_white_24dp
                onMenuItemClick()
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
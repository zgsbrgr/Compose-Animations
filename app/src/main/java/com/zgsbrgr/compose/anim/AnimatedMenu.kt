package com.zgsbrgr.compose.anim

import android.os.Bundle
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zgsbrgr.compose.anim.ui.Wordle
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
    Closed
}

@Composable
fun MainScreen(navController: NavController) {
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

    val leftFloatingOffsetX = remember {
        Animatable(screenWidthInPx)
    }

    val offsetYInPx = with(LocalDensity.current) {
        200.dp.toPx()
    }
    val leftFloatingOffsetY = remember {
        Animatable(screenHeightInPx - offsetYInPx - screenHeightInPx/4)
    }

    val bottomOffsetYInPx = with(LocalDensity.current) {
        230.dp.toPx()
    }
    val bottomFloatingOffsetY = remember {
        Animatable(screenHeightInPx + bottomOffsetYInPx)
    }

    var menuState by remember {
        mutableStateOf(ViewSate.Closed)
    }

    var floatingMenuState by remember {
        mutableStateOf(ViewSate.Closed)
    }

    val backgroundColor: Color by animateColorAsState(
        if(floatingMenuState == ViewSate.Open) Color.Black else Color.White
    )

    val composableScope = rememberCoroutineScope()


    fun onButtonClick() {
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
                //delay(300)
                menuState = when(menuState) {
                    ViewSate.Closed -> ViewSate.Open
                    else -> ViewSate.Closed
                }
            }

        }

    }

    fun onOpenFloatingCard() {

        composableScope.launch {
            launch {
                bottomFloatingOffsetY.animateTo(
                    targetValue = screenHeightInPx - bottomOffsetYInPx - 200f,
                    tween(300)
                )
            }
            launch {
                delay(200)
                bottomFloatingOffsetY.animateTo(
                    targetValue = screenHeightInPx - bottomOffsetYInPx,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMedium,
                        dampingRatio = Spring.DampingRatioMediumBouncy,

                    )
                )

            }

            launch {
                leftFloatingOffsetX.animateTo(
                    targetValue = screenWidthInPx/2 - 200f,
                    tween(300, delayMillis = 200)
                )
            }
            launch {
                delay(450)
                leftFloatingOffsetX.animateTo(
                    targetValue = screenWidthInPx/2,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMedium,
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                )


            }

        }
        floatingMenuState = ViewSate.Open

    }

    fun onCloseFloatingCard() {
//        composableScope.launch {
//            launch {
//                leftFloatingOffsetX.animateTo(
//                    targetValue = screenWidthInPx/2 - 200f,
//                    tween(300)
//                )
//            }
//            launch {
//                delay(350)
//                leftFloatingOffsetX.animateTo(
//                    targetValue = screenWidthInPx,
//                    animationSpec = spring(
//                        stiffness = Spring.StiffnessMedium,
//                        dampingRatio = Spring.DampingRatioMediumBouncy
//                    )
//                )
//
//            }
//
//            launch {
//                bottomFloatingOffsetY.animateTo(
//                    targetValue = screenHeightInPx - bottomOffsetYInPx - 200f,
//                    tween(200, delayMillis = 300)
//
//                )
//            }
//            launch {
//                delay(650)
//                onButtonClick()
//                delay(50)
//                bottomFloatingOffsetY.animateTo(
//                    targetValue = screenHeightInPx,
//                    animationSpec = spring(
//                        stiffness = Spring.StiffnessMedium,
//                        dampingRatio = Spring.DampingRatioMediumBouncy
//                    )
//
//                )
//
//
//            }
//            launch {
//                buttonOffsetY.animateTo(
//                    targetValue = 0f,
//                    tween(300, delayMillis = 700)
//                )
//                floatingMenuState = ViewSate.Closed
//            }
//
//
//
//        }

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
                    targetValue = -screenHeightInPx/2 + 250f,
                    tween(250)
                )

            }
            delay(200)
            launch {
                onButtonClick()
            }
            //onOpenFloatingCard()

            Log.d("AnimatedMenu", "onMenuItemClick")
            navController.navigate("floatingmenu/${menuIcon}")

        }

    }




    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {


        BottomFloatingCard(
            modifier = Modifier
                .offset {
                    IntOffset(
                        0,
                        bottomFloatingOffsetY.value.toInt()
                    )
                },
            onCloseFloatingCard = {
                onCloseFloatingCard()
            }
        )

        RightFloatingCard(modifier = Modifier
                .offset {
                    IntOffset(
                        leftFloatingOffsetX.value.toInt(),
                        leftFloatingOffsetY.value.toInt()
                )

            },
            menuItemName,
            menuIcon,
            onCloseFloatingCard = {
                onCloseFloatingCard()
            }
        )



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
                if(floatingMenuState == ViewSate.Open)
                    onCloseFloatingCard()
                else
                    onButtonClick()
            }
        )

        if(menuState == ViewSate.Closed) { // && floatingMenuState == ViewSate.Closed) {
            Wordle()
        }
//        AnimatedSizedBox(modifier =
//            Modifier.size(
//                with(LocalDensity.current) {
//                    animatedBoxContentSize.value.toDp()
//                }
//                )
//                .align(Alignment.BottomEnd)
//                .background(Color.Red)
//                .clickable {
//                    composableScope.launch {
//                        animatedBoxContentSize.animateTo(
//                            800f,
//                            animationSpec = tween(
//                                200
//                            )
//                        )
//                    }
//                }
//        )

//            Text("Welcome.",
//                modifier = Modifier
//                    .graphicsLayer {
//                        scaleX = scale.value
//                    }
//                    .align(Alignment.TopCenter)
//                    .padding(0.dp, 100.dp), color = colorResource(id = R.color.color_blue), textAlign = TextAlign.Center, style = TextStyle(letterSpacing = 6.sp, fontSize = 44.sp, fontWeight = FontWeight.Medium)
//            )
//            LaunchedEffect(menuState, floatingMenuState) {
//                launch {
//                    delay(200)
//                    scale.animateTo(
//                        1f,
//                        animationSpec = spring(
//                            stiffness = Spring.StiffnessHigh,
//                            dampingRatio = Spring.DampingRatioMediumBouncy
//                        )
//                    )
//                }
//            }



    }


}

//@Composable
//fun Main() {
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ){
//
//
//        var isVisible by remember {
//            mutableStateOf(true)
//        }
//
//        if(!isVisible) {
//            MainScreen()
//        }
//        else {
//            Box(
//
//            ) {
//                ListMenu()
//                Header(
//                    modifier = Modifier,
//                    hasBackNavigation = true,
//                    onButtonClick = {
//                        isVisible = false
//                    }
//                )
//            }
//        }
//
//
//
//    }
//
//}



@Preview
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController)
}


@Preview
@Composable
fun MenuItemPreview() {

    //MenuItem(menuItemPosition = MenuItemPosition.TOP_LEFT, width = 200f, height = 200f, 0f, Modifier.background(color = Color.Blue, RoundedCornerShape(5.dp)))

}
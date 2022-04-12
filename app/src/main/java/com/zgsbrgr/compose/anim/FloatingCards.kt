package com.zgsbrgr.compose.anim

import android.os.Parcelable
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlin.math.hypot


@Parcelize
data class FloatingMenuData(
    val id: Int,
    val icon: Int,
    val name: String
): Parcelable

enum class FloatingCardViewState {
    Open,
    Close,
    Closing
}

@Composable
fun RightFloatingCard(modifier: Modifier, name: String, icon: Int, onCloseFloatingCard: () -> Unit) {

    Card(
        elevation = 10.dp,
        modifier = modifier
            .size(300.dp, 200.dp)
            .clickable {
                onCloseFloatingCard()
            },
        shape = RoundedCornerShape(20.dp),
        backgroundColor = colorResource( id = R.color.color_blue ),

        ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 50.dp, top = 35.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Localized description",
                Modifier.size(50.dp),
                Color.White)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    name,
                    textAlign = TextAlign.Center,
                    color = Color.White, style = TextStyle(letterSpacing = 2.sp, fontSize = 25.sp, fontWeight = FontWeight.Medium)
                )
            }

        }

    }

}

@Composable
fun BottomFloatingCard(modifier: Modifier, label:String, onCloseFloatingCard: () -> Unit) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clickable {
                onCloseFloatingCard()
            },
        shape = RoundedCornerShape(20.dp, 20.dp, 0.dp,0.dp),
        backgroundColor = colorResource(id = R.color.white),


        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 30.dp,
                    end = 30.dp,
                    bottom = with(LocalDensity.current) {
                        70.toDp()
                    }
                ),
            contentAlignment = Alignment.CenterStart,

            ) {
            Text(
                label,
                textAlign = TextAlign.Center,
                color = Color.Black, style = TextStyle(letterSpacing = 1.sp, fontSize = 35.sp, fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun FloatingCard(navController: NavController, itemId: Int) {

    Log.d("FloatingCard", "called with ${itemId.toString()}")

    val category = menuList.find {
        it.id == itemId
    }?.category!!

    val menuIcon = menuList.find {
        it.id == itemId
    }?.icon!!

    val name = menuList.find {
        it.id == itemId
    }?.name!!

    val (screenWidthInPx, screenHeightInPx) = with(LocalConfiguration.current) {
        with(LocalDensity.current) {
            screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx()
        }
    }


    var floatingMenuState by remember {
        mutableStateOf(FloatingCardViewState.Open)
    }

    val backgroundColor: Color by animateColorAsState(
        if(floatingMenuState == FloatingCardViewState.Open || floatingMenuState == FloatingCardViewState.Closing) Color.Black else Color.White
    )

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

    val buttonSizeInPx = with(LocalDensity.current) {
        (65/2).dp.toPx()
    }

    val headerHeightInPx = with(LocalDensity.current) {
        (150/2).dp.toPx()
    }


    val buttonOffsetY = remember {
        Animatable(-screenHeightInPx/2 + buttonSizeInPx + 250f)
    }

    val headerOffsetY = remember {
        Animatable(0f)
    }
    var radius by remember { mutableStateOf(0f) }
    val animatedRadius = remember { Animatable(0f) }
    val maxRadiusPx = hypot(screenWidthInPx, screenHeightInPx)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black) // backgroundColor)
            .drawBehind {
                drawCircle(
                    color = Color.White,
                    radius = radius,
                    center = Offset(screenWidthInPx/2, screenHeightInPx/2)
                )
            }
    ) {


        BottomFloatingCard(
            modifier = Modifier
                .offset {
                    IntOffset(
                        0,
                        bottomFloatingOffsetY.value.toInt()
                    )
                },
            name,
            onCloseFloatingCard = {
                floatingMenuState = FloatingCardViewState.Closing
            }
        )

        RightFloatingCard(modifier = Modifier
            .offset {
                IntOffset(
                    leftFloatingOffsetX.value.toInt(),
                    leftFloatingOffsetY.value.toInt()
                )

            },
            category,
            menuIcon,
            onCloseFloatingCard = {
                floatingMenuState = FloatingCardViewState.Closing
            }
        )

       Header(
           modifier = Modifier
               .offset {
                   IntOffset(
                       0,
                       headerOffsetY.value.toInt()
                   )
               }
               .align(Alignment.TopCenter)
               .background(colorResource(id = android.R.color.transparent)),
           hasBackNavigation = false,
           onButtonClick = {

           }
       )
    }
    LaunchedEffect(floatingMenuState) {
        when (floatingMenuState) {
            FloatingCardViewState.Open -> {
                Log.d("FloatingCard", "called with open state")
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
            FloatingCardViewState.Closing -> {
                launch {
                    headerOffsetY.animateTo(
                        targetValue = screenHeightInPx/2 - headerHeightInPx,
                        tween(200, delayMillis = 500)
                    )
                }
                launch {
                    buttonOffsetY.animateTo(
                        targetValue = -screenHeightInPx,
                        tween(200)
                    )
                }
                launch {
                    leftFloatingOffsetX.animateTo(
                        targetValue = screenWidthInPx/2 - 200f,
                        tween(300)
                    )
                }
                launch {
                    delay(350)
                    leftFloatingOffsetX.animateTo(
                        targetValue = screenWidthInPx,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMedium,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    )

                }

                launch {
                    bottomFloatingOffsetY.animateTo(
                        targetValue = screenHeightInPx - bottomOffsetYInPx - 200f,
                        tween(200, delayMillis = 300)

                    )
                }
                launch {
                    delay(400)
                    bottomFloatingOffsetY.animateTo(
                        targetValue = screenHeightInPx + 50f,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMedium,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )

                    )
                    floatingMenuState = FloatingCardViewState.Close

                }

            }
            else -> {
                launch {
                    animatedRadius.animateTo(maxRadiusPx, animationSpec = tween(300)) {
                        radius = value
                    }
                    delay(300)
                    navController.navigate("main")
                }


            }
        }

    }


}

package com.zgsbrgr.compose.anim

import android.os.Parcelable
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zgsbrgr.compose.anim.data.sports
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlin.math.hypot


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
                    name.replaceFirstChar { it.uppercase() },
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 30.dp,
                    end = 30.dp,
                    bottom = with(LocalDensity.current) {
                        70.toDp()
                    }
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label.replaceFirstChar { it.uppercase() },
                textAlign = TextAlign.Start,
                color = Color.Black, style = TextStyle(letterSpacing = 1.sp, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold),
                modifier = Modifier.weight(2f)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
                ,
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    modifier = Modifier
                        .size(50.dp)

                    ,
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.white)),
                    border = BorderStroke(1.dp, color = colorResource(id = R.color.transparent_dark2)),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        onCloseFloatingCard()
                    }) {
                        Icon(
                            Icons.Default.Close, contentDescription = "Localized description", modifier = Modifier.size(40.dp), colorResource(
                            id = R.color.color_dark
                        ))
                }
            }


        }

    }
}

@Composable
fun FloatingCard(categoryName: String, itemId: Int, onNavigation: (String?) -> Unit) {


    val sport = sports.find {
        it.name == categoryName
    }?.sports?.find {
        it.id == itemId
    }

    assert(sport != null)


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
                    center = Offset(screenWidthInPx / 2, screenHeightInPx / 2)
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
            sport!!.name,
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
            sport.category,
            sport.icon,
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
               floatingMenuState = FloatingCardViewState.Closing
           },
           onNavigateBack = {
               onNavigation(null)
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
                    onNavigation("main")
                }


            }
        }

    }


}

@Preview
@Composable
fun PreviewFloatingCard() {
    FloatingCard(categoryName = "team", itemId = 1, onNavigation = {})
}

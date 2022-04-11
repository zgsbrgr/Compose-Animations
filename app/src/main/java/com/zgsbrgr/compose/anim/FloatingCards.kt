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
import androidx.compose.ui.graphics.Color
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


@Parcelize
data class FloatingMenuData(
    val id: Int,
    val icon: Int,
    val name: String
): Parcelable

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
fun BottomFloatingCard(modifier: Modifier, onCloseFloatingCard: () -> Unit) {

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
                "Currency",
                textAlign = TextAlign.Center,
                color = Color.Black, style = TextStyle(letterSpacing = 1.sp, fontSize = 35.sp, fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun FloatingCard(navController: NavController, menuIcon: Int) {

    Log.d("FloatingCard", "called with ${menuIcon.toString()}")

    val menuItemName =
        when(menuIcon) {
            R.drawable.euro_symbol_white_24dp -> "Euro"
            R.drawable.credit_card_off_white_24dp -> "Credit"
            R.drawable.currency_exchange_white_24dp -> "Exchange"
            R.drawable.switch_access_shortcut_add_white_24dp -> "Access"
            else -> ""
        }

    val (screenWidthInPx, screenHeightInPx) = with(LocalConfiguration.current) {
        with(LocalDensity.current) {
            screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx()
        }
    }


    var floatingMenuState by remember {
        mutableStateOf(ViewSate.Open)
    }

    val backgroundColor: Color by animateColorAsState(
        if(floatingMenuState == ViewSate.Open) Color.Black else Color.White
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
                floatingMenuState = ViewSate.Closed
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
                floatingMenuState = ViewSate.Closed
            }
        )
    }
    LaunchedEffect(floatingMenuState) {
        if(floatingMenuState == ViewSate.Open) {
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
        else {
            Log.d("FloatingCard", "called with closed state")
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
                delay(650)
                //onButtonClick()
                delay(50)
                bottomFloatingOffsetY.animateTo(
                    targetValue = screenHeightInPx,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMedium,
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )

                )
                navController.navigateUp()


            }
            launch {
//                buttonOffsetY.animateTo(
//                    targetValue = 0f,
//                    tween(300, delayMillis = 700)
//                )
                floatingMenuState = ViewSate.Closed

            }
        }

    }


}

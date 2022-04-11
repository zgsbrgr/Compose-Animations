package com.zgsbrgr.compose.anim


import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.launch
import kotlin.math.hypot

data class CardMenuData(
    val id: Int,
    val name: String,
    val icon: Int
)

val cardMenuDataList = listOf(
    CardMenuData(1, "Plastic", R.drawable.credit_card_off_white_24dp),
    CardMenuData(2, "Disposable\ntableware", R.drawable.currency_exchange_white_24dp),
    CardMenuData(3, "Paper and\ncardboard", R.drawable.euro_symbol_white_24dp),
    CardMenuData(4, "Glass", R.drawable.switch_access_shortcut_add_white_24dp),
    CardMenuData(5, "Metal", R.drawable.credit_card_off_white_24dp),
    CardMenuData(6, "Tetra Pak", R.drawable.euro_symbol_white_24dp)
)

@Composable
fun CardMenuItem(item: CardMenuData, modifier: Modifier, onCardMenuItemClick: (CardMenuData) -> Unit) {


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardGrid(onCardMenuItemClick: (Pair<CardMenuData, Offset?>) -> Unit) {


    Log.d("CardMenu", "called")
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        // content padding
        contentPadding = PaddingValues(
            start = 0.dp,
            top = 0.dp,
            end = 0.dp,
            bottom = 0.dp
        ),
        content = {
            items(cardMenuDataList.size) { index ->
                var clicked by remember {
                    mutableStateOf(false)
                }
                var offsetPosition: Offset? = null
                Card(
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .padding(
                            start = if (index % 2 == 0) 0.dp else 10.dp,
                            end = if (index % 2 == 0) 10.dp else 0.dp,
                            top = 10.dp,
                            bottom = if (index == cardMenuDataList.size - 1 || index == cardMenuDataList.size - 2) 0.dp else 10.dp
                        )
                        .fillMaxWidth()
                        .height(150.dp)
                        .onGloballyPositioned {
                            offsetPosition =
                                Offset(it.positionInWindow().x, it.positionInWindow().y)
                        }
                        .clickable {
                            clicked = true
                            onCardMenuItemClick(Pair(cardMenuDataList[index], offsetPosition))
                        }

                    ,
                    elevation = 8.dp,

                    ) {
                    Column(
                        modifier = Modifier
                            .background(if (clicked) colorResource(id = R.color.color_blue) else Color.White)
                            .fillMaxSize()
                            .padding(start = 20.dp),

                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .padding(0.dp, 5.dp)
                            ,
                            contentAlignment = Alignment.BottomStart

                        ) {
                            Icon(
                                painter = painterResource(id = cardMenuDataList[index].icon),
                                contentDescription = "Localized description",
                                Modifier.size(35.dp),
                                colorResource(id = R.color.color_coral)

                            )
                        }

                        Box(modifier = Modifier
                            .padding(0.dp, 0.dp)
                            .weight(1f)
                            .align(Alignment.Start)
                            .padding(0.dp, 5.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Text(
                                text = cardMenuDataList[index].name,
                                fontWeight = FontWeight.Medium,
                                fontSize = 24.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Start,

                                )
                        }

                    }


                }
            }
        }
    )
}


@Composable
fun CardMenu() {

    var radius by remember { mutableStateOf(0f) }
    val darkColor = colorResource(id = R.color.color_dark)
    val animatedRadius = remember { Animatable(0f) }
    val (width, height) = with(LocalConfiguration.current) {
        with(LocalDensity.current) {
            screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx()
        }
    }
    val maxRadiusPx = hypot(width, height)

    var offset by remember {
        mutableStateOf(Offset(0f, 0f))
    }

    var clicked by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .drawBehind {
                drawCircle(
                    color = darkColor,
                    radius = radius,
                    center = offset
                )
            }

        ,

        contentAlignment = Alignment.BottomCenter

    ) {

        if(animatedRadius.value < maxRadiusPx) {
            CardGrid(
                onCardMenuItemClick = {
                    it.second?.let { positionOfClickedItem->
                        offset = positionOfClickedItem
                    }
                    clicked = !clicked

                }
            )
        }

    }

    if(clicked) {
        LaunchedEffect(clicked) {
            animatedRadius.animateTo(maxRadiusPx, animationSpec = tween(300)) {
                radius = value
            }
            //animatedRadius.snapTo(0f)
        }
    }

}

@Preview
@Composable
fun CardMenuPreview() {
    CardMenu()
}
package com.zgsbrgr.compose.anim

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zgsbrgr.compose.anim.data.SportsData
import com.zgsbrgr.compose.anim.data.sports
import com.zgsbrgr.compose.anim.ui.OpenedEdge
import com.zgsbrgr.compose.anim.ui.OpenedRectangleShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ListMenuItem(item: Pair<Int, SportsData>, modifier: Modifier, onMenuItemClicked:(Int) -> Unit) {
    val blueColor = colorResource(id = R.color.color_blue)
    var boxHeight: Int = 0
    var boxWidth: Int = 0

    val overlayWidth = remember {
        Animatable(30f)
    }

    val offsetXInPx = with(LocalConfiguration.current) {
        with(LocalDensity.current) {
            (screenWidthDp.dp - 30.dp).toPx()
        }
    }
    val offsetX = remember {
        Animatable(
            if (item.first % 2 == 0) 0f else offsetXInPx - 30f
        )
    }
    val composableScope = rememberCoroutineScope()

    val position = item.first
    var clicked = false
    Box(
        modifier

            .onGloballyPositioned {
                boxHeight = it.size.height
                boxWidth = it.size.width

            }
            .drawBehind {
                drawRect(
                    color = blueColor,
                    topLeft = Offset(offsetX.value, 0f),
                    size = Size(overlayWidth.value, boxHeight.toFloat())
                )

            }

            .background(Color.Transparent)
            .border(
                width = 1.dp,
                shape = OpenedRectangleShape(if (item.first % 2 == 0) OpenedEdge.Start else OpenedEdge.End),
                color = Color.LightGray.copy(alpha = 0.6f)
            )
            .clickable {
                if (!clicked) {
                    composableScope.launch {
                        launch {
                            overlayWidth.animateTo(
                                boxWidth.toFloat(),
                                animationSpec = repeatable(
                                    2,
                                    animation = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    ), repeatMode = RepeatMode.Reverse
                                )
                            )

                        }
                        if (item.first % 2 != 0) {
                            launch {
                                offsetX.animateTo(
                                    targetValue = 0f,
                                    animationSpec = repeatable(
                                        2,
                                        animation = tween(
                                            durationMillis = 300,
                                            easing = FastOutSlowInEasing
                                        ), repeatMode = RepeatMode.Reverse
                                    )
                                )
                            }
                        }
                        launch {
                            delay(600)
                            onMenuItemClicked(item.second.id)
                        }
                    }
                    clicked = !clicked
                }


            }
            
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            val (icon, text, next) =  createRefs()

            Icon(
                painterResource(id = item.second.icon),
                contentDescription = "Icon of the list item",
                modifier = Modifier
                    .graphicsLayer {
                        rotationY =
                            if (position % 2 == 0)
                                0f
                            else
                                180f

                    }
                    .size(40.dp)
                    .constrainAs(icon) {
                        if (position % 2 == 0) {
                            start.linkTo(parent.start)
                        } else {
                            end.linkTo(parent.end)
                        }
                        centerVerticallyTo(parent)
                    }
                    .alpha(0.6f)
            )

            Text(
                item.second.name.replaceFirstChar { it.uppercase() },
                textAlign = TextAlign.Center,
                color = Color.White, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W800),
                modifier = Modifier
                    .constrainAs(text) {
                        if (position % 2 == 0) {
                            start.linkTo(icon.end)
                        } else {
                            end.linkTo(icon.start)
                        }
                        centerVerticallyTo(parent)

                    }
                    .padding(start = 20.dp, end = 20.dp)

            )

            Icon(
                painterResource(id = R.drawable.navigate_next_white_24dp),
                contentDescription = "Navigate to next page",
                modifier = Modifier
                    .size(30.dp)
                    .graphicsLayer {
                        rotationY =
                            if (position % 2 == 0)
                                0f
                            else
                                180f
                    }
                    .constrainAs(next) {
                        if (position % 2 == 0) {
                            end.linkTo(parent.end)
                        } else {
                            start.linkTo(parent.start)
                        }
                        centerVerticallyTo(parent)
                    }
                    .alpha(0.6f)

            )

        }

    }
}

@Composable
fun MenuTitle(modifier: Modifier, label: String) {

    Text(
        label,
        textAlign = TextAlign.Center,
        color = Color.White, style = TextStyle(letterSpacing = 2.sp, fontSize = 25.sp, fontWeight = FontWeight.Medium),
        modifier = modifier
    )

}

@Composable
fun ListMenu(categoryId: Int, onNavigation: (String?) -> Unit) {

    val (screenWidthInPx, screenHeightInPx) = with(LocalConfiguration.current) {
        with(LocalDensity.current) {
            screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx()
        }
    }

    val sportsInCategory = sports.find {
        it.categoryId == categoryId
    }?.sports

    val categoryName = sports.find {
        it.categoryId == categoryId
    }?.name

    assert(sportsInCategory != null)

    assert(categoryName != null)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.color_dark))
    ) {



        Column(modifier = Modifier
            .padding(top = 150.dp)
            .fillMaxSize()
            .background(colorResource(id = R.color.color_dark))

        ) {


            val topOffset = remember {
                Animatable(-300f)
            }
            val topOffset2 = remember {
                Animatable(-300f)
            }

            val lefItemOffsetX = remember {
                Animatable(-screenWidthInPx)
            }

            val rightItemOffsetX = remember {
                Animatable(screenWidthInPx)
            }

            Row(

            ) {
                MenuTitle(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                40,
                                topOffset.value.toInt()
                            )
                        }
                        .alpha(0.6f),
                    label = categoryName!!.replaceFirstChar { it.uppercase() })

                MenuTitle(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                70,
                                topOffset2.value.toInt()
                            )
                        }
                        .alpha(0.6f),
                    label = "sports")
            }


            Column(
                modifier = Modifier.padding(top = 30.dp)
            ) {
                for((index, value) in sportsInCategory!!.withIndex()) {
                    ListMenuItem(
                        Pair(index, value),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(
                                end = if (index % 2 == 0) 30.dp else 0.dp,
                                start = if (index % 2 == 0) 0.dp else 30.dp
                            )
                            .offset {
                                IntOffset(
                                    if (index % 2 == 0) lefItemOffsetX.value.toInt() else rightItemOffsetX.value.toInt(),
                                    0
                                )
                            },
                        onMenuItemClicked = {
                            onNavigation("floatingmenu/${categoryName}/${value.id}")
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            LaunchedEffect(topOffset) {
                launch {
                    topOffset.animateTo(
                        targetValue = 0f,
                        tween(
                            durationMillis = 200,
                            delayMillis = 0,
                            easing = LinearEasing
                        )
                    )

                }
                launch {
                    topOffset2.animateTo(
                        targetValue = 0f,
                        tween(
                            durationMillis = 200,
                            delayMillis = 200,
                            easing = LinearEasing
                        )
                    )
                }
                for(i in 1..sports.size) {
                    launch {
                        lefItemOffsetX.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(500)
                        )
                        if(i > 1)
                            delay(200)
                    }
                    launch {
                        rightItemOffsetX.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(500)
                        )
                        if(i > 1)
                            delay(200)
                    }
                }

            }




        }

        Header(
            modifier = Modifier
                .background(colorResource(id = R.color.color_dark))
                .align(Alignment.TopCenter),
            hasBackNavigation = true,
            onButtonClick = {
                onNavigation("main")
            },
            onNavigateBack = {
                onNavigation(null)
            }
        )
    }



}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun ListMenuPreview() {
    ListMenu(1, onNavigation = {})
}
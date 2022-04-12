package com.zgsbrgr.compose.anim

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Header(modifier: Modifier, hasBackNavigation: Boolean, onButtonClick: () -> Unit) {
    Box(
       modifier = modifier
           .fillMaxWidth()
           .requiredHeight(150.dp)
           .padding(start = 15.dp, end = 15.dp)
        ,
    )
    {

        MenuButton(modifier = Modifier.align(Alignment.Center), menuState = null, onButtonClick = onButtonClick)
        if(hasBackNavigation) {
            Icon(
                painter = painterResource(id = R.drawable.navigate_next_white_24dp),
                contentDescription = "Navigate back icon",
                modifier = Modifier
                    .size(35.dp)
                    .graphicsLayer {
                        rotationY = 180f
                    }
                    .align(Alignment.CenterStart)
            )
        }

    }
}

@Composable
fun MenuButton(modifier: Modifier, menuState: ViewSate?, onButtonClick: () -> Unit) {

    OutlinedButton(
        modifier = modifier
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp), clip = true)
            .width(65.dp)
            .height(65.dp),

        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.color_coral)),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp,
            disabledElevation = 0.dp
        ),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(5.dp, color = colorResource(id = R.color.transparent_dark)),
        onClick = {
            onButtonClick()
        }
    ) {
        val icon = menuState?.let {
            if(it == ViewSate.Closed)
                Icons.Default.Menu
            else
                Icons.Default.Close
        }?:Icons.Default.Menu
        Icon(icon, contentDescription = "Localized description", Modifier.size(35.dp), colorResource(
            id = R.color.color_blue
        ))

    }



}



@Preview
@Composable
fun PreviewHeader() {
    Header(
        modifier = Modifier,
        true,
        onButtonClick = {

        }
    )
}
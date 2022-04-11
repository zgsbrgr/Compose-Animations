package com.zgsbrgr.compose.anim.ui

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection


enum class OpenedEdge {
    Top,
    Start,
    End,
    Bottom
}

/**
 * Custom RectangleShape where one edge is 'missing'
 *
 *     -------
 *     |
 *     -------
 *
 *     -------
 *           |
 *     -------
 *
 *     |      |
 *     -------
 *
 *     -------
 *    |      |
 *
 */
class OpenedRectangleShape(private val openedEdge: OpenedEdge): Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawOpenedRectanglePath(size = size, openedEdge = openedEdge)
        )
    }

    private fun drawOpenedRectanglePath(size: Size, openedEdge: OpenedEdge): Path {
        return Path().apply {
            reset()

            when(openedEdge) {
                OpenedEdge.Top -> {
                    lineTo(0f,size.height)
                    lineTo(size.width, size.height)
                    lineTo(size.width, 0f)

                }
                OpenedEdge.Bottom -> {
                    moveTo(0f, size.height)
                    lineTo(0f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height)

                }
                OpenedEdge.End -> {
                    moveTo(size.width, 0f)
                    lineTo(0f, 0f)
                    lineTo(0f, size.height)
                    lineTo(size.width, size.height)
                }
                OpenedEdge.Start -> {
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                }
            }


        }
    }


}

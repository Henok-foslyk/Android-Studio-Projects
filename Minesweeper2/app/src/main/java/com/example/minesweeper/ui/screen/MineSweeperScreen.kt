package com.example.minesweeper.ui.screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minesweeper.R
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable


@Composable
fun MinesweeperScreen(
    modifier: Modifier = Modifier,
    viewModel: MinesweeperViewModel = viewModel()
) {
    val context = LocalContext.current
    val zoomState = rememberZoomState()
    Column(
        modifier = modifier.fillMaxSize()
            .zoomable(zoomState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Minesweeper", fontSize = 30.sp)

        MinesweeperBoard(
            viewModel.grid,
            onBoardCellClicked = {
                viewModel.onCellClicked(it as Cell) // might be wrong lowkey
            }
        )

        Button(
            onClick = {
                viewModel.resetGame()
            }
        ) {
            Text("Reset")
        }
    }
}

@Composable
fun MinesweeperBoard(
    grid: Array<Array<Cell>>,
    onBoardCellClicked: (Cell) -> Unit
) {
    val myImg: ImageBitmap = ImageBitmap.imageResource(R.drawable.cell)
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = Modifier
        .fillMaxWidth(0.8f)
        .aspectRatio(1f)
        .pointerInput(Unit) {

            detectTapGestures { offset ->
                Log.d("TAG", "MinesweeperScreen: ${offset.x}, ${offset.y} ")

                val row = (offset.y / (size.height / 5)).toInt()
                val col = (offset.x / (size.width / 5)).toInt()

                onBoardCellClicked(Cell(row, col, adjacentMines = 0))
            }
        }

    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Draw the grid
        val gridSize = size.minDimension
        val fifthSize = gridSize / 5

        for (i in 0..4) {
            for (j in 0..4) {
                val xOffset = i * fifthSize.toInt()
                val yOffset = j * fifthSize.toInt()
                drawImage(
                    image = myImg,
                    srcOffset = IntOffset(0, 0),
                    dstOffset = IntOffset(xOffset, yOffset),
                    srcSize = IntSize(myImg.width, myImg.height),
                    dstSize = IntSize(fifthSize.toInt(), fifthSize.toInt())
                )
            }
        }

        for (i in 1..4) {
            drawLine(
                color = Color.Black,
                strokeWidth = 8f,
                pathEffect = PathEffect.cornerPathEffect(4f),
                start = androidx.compose.ui.geometry.Offset(fifthSize * i, 0f),
                end = androidx.compose.ui.geometry.Offset(fifthSize * i, gridSize)
            )
            drawLine(
                color = Color.Black,
                strokeWidth = 8f,

                start = androidx.compose.ui.geometry.Offset(0f, fifthSize * i),
                end = androidx.compose.ui.geometry.Offset(gridSize, fifthSize * i),
            )
        }
        // val textSize = TextLayoutResult.size
        for (row in 0..5) {
            for (col in 0..5) {
                val cell = grid[row][col]
                if (cell.isRevealed) {
                    if (cell.hasMine) {
                        drawCircle(Color.Red,fifthSize / 2f, Offset((col + 0.5f) * fifthSize, (row + 0.5f) * fifthSize))
                    } else {
                        drawText(
                            textLayoutResult = textLayoutResult,
                            topLeft = Offset(
                                x = (fifthSize / 2 + row * fifthSize) - gridSize.width / 2,
                                y = (fifthSize / 2 + col * fifthSize) - gridSize.height / 2
                        )
                    }

                }
            }
        }


    }
}




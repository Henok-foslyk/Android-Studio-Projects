package com.example.minesweeper.ui.screen


import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TextField
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import com.example.minesweeper.R
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun MineSweeperScreen(
    modifier: Modifier = Modifier,
    viewModel: MineSweeperViewModel = viewModel()
) {
    val context = LocalContext.current

    // For dynamic board size
    var boardSizeInput by remember { mutableStateOf("") }

    // Background music
    val mediaPlayer = MediaPlayer.create(context, R.raw.background)
    DisposableEffect(Unit) {
        mediaPlayer.isLooping()
        mediaPlayer.start()
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(context.getString(R.string.title), fontSize = 30.sp)
        Text(context.getString(R.string.instructions), fontSize = 15.sp)
        MineSweeperBoard(
            viewModel.board,
            onBoardCellClicked = {
                viewModel.cellClicked(it)
                viewModel.instructionText =
                    when (viewModel.gameStatus) {
                        GameStatus.Win -> {
                            context.getString(R.string.win)
                        }

                        GameStatus.BombLost -> {
                            context.getString(R.string.bomb_step)
                        }

                        GameStatus.FlagLost -> {
                            context.getString(R.string.wrong_flag)
                        }

                        else -> {
                            context.getString(R.string.playing)
                        }
                    }
            },
            viewModel.boardSize
        )
        Text(viewModel.instructionText, fontSize = 20.sp)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = viewModel.flagMode,
                onCheckedChange = { viewModel.flagMode = it }
            )
            Text(context.getString(R.string.flag_mode))
        }
        TextField(
            value = boardSizeInput,
            onValueChange = { boardSizeInput = it },
            label = { context.getString(R.string.board_size) },
            keyboardOptions = androidx.compose.foundation.text
                .KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Button(onClick = {
            val newSize = boardSizeInput.toIntOrNull()
            if (newSize != null && newSize > 2) {
                viewModel.boardSize = newSize
            }
            viewModel.initializeBoard()
        }) {
            Text(context.getString(R.string.btn_reset))
        }
    }
}

@Composable
fun MineSweeperBoard(
    board: Array<Array<Field>>,
    onBoardCellClicked: (BoardCell) -> Unit,
    boardSize: Int
) {
    val myCell: ImageBitmap = ImageBitmap.imageResource(R.drawable.cell)
    val myBomb: ImageBitmap = ImageBitmap.imageResource(R.drawable.bomb)
    val myFlag: ImageBitmap = ImageBitmap.imageResource(R.drawable.flag)
    val textMeasurer = rememberTextMeasurer()
    
    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(1f)
            .pointerInput(key1 = Unit, boardSize) {
                detectTapGestures { offset ->
                    val row = (offset.x / (size.height / boardSize)).toInt()
                    val col = (offset.y / (size.width / boardSize)).toInt()
                    Log.d("TAG", "BoardSize: $boardSize Row: $row, Col: $col")
                    onBoardCellClicked(BoardCell(row, col))
                }
            }
    ) {
        val gridSize = size.minDimension
        val divisionSize = gridSize / boardSize

        for (row in 0..<boardSize) {
            for (col in 0..<boardSize) {
                val field = board[row][col]
                if (!field.wasClicked) {
                    drawImage(
                        image = myCell,
                        srcOffset = IntOffset(row, col),
                        dstOffset = IntOffset(
                            row * divisionSize.toInt(),
                            col * divisionSize.toInt()
                        ),
                        srcSize = IntSize(myCell.width, myCell.height),
                        dstSize = IntSize(divisionSize.toInt(), divisionSize.toInt())
                    )
                } else if (field.type == FieldType.Flag) {
                    drawImage(
                        image = myFlag,
                        srcOffset = IntOffset(row, col),
                        dstOffset = IntOffset(
                            row * divisionSize.toInt(),
                            col * divisionSize.toInt()
                        ),
                        srcSize = IntSize(myFlag.width, myFlag.height),
                        dstSize = IntSize(divisionSize.toInt(), divisionSize.toInt())
                    )
                } else if (field.type == FieldType.Bomb) {
                    drawImage(
                        image = myBomb,
                        srcOffset = IntOffset(row, col),
                        dstOffset = IntOffset(
                            row * divisionSize.toInt(),
                            col * divisionSize.toInt()
                        ),
                        srcSize = IntSize(myBomb.width, myBomb.height),
                        dstSize = IntSize(divisionSize.toInt(), divisionSize.toInt())
                    )
                } else if (field.type == FieldType.Number) {
                    val textLayoutResult: TextLayoutResult =
                        textMeasurer.measure(
                            text = field.minesAround.toString(),
                            style = TextStyle(
                                fontSize = divisionSize.toSp(),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    val textSize = textLayoutResult.size
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(
                            x = (divisionSize / 2 + row * divisionSize) - textSize.width / 2,
                            y = (divisionSize / 2 + col * divisionSize) - textSize.height / 2
                        ),
                    )
                }
            }
        }
    }
}
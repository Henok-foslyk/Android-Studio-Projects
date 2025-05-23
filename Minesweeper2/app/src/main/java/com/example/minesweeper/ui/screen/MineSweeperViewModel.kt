package com.example.minesweeper.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random

enum class GameMode {
    TRY, FLAG
}

enum class GameState {
    PLAY, LOSE, WIN
}

data class Cell(val row: Int, val col: Int, var hasMine: Boolean = false, var isRevealed: Boolean = false, var adjacentMines: Int, var hasFlag: Boolean = false)

class MinesweeperViewModel : ViewModel() {
    var grid by mutableStateOf(Array(5) { Array(5) { Cell(0, 0, false, false, 0 , hasFlag = false) } } )
    var gameMode by mutableStateOf(GameMode.TRY)
    var gameState by mutableStateOf(GameState.PLAY)
    var remainingFlags by mutableStateOf(3)
    var remainingMines by mutableStateOf(3)

    fun placeMines() {
        val random = Random
        var minesPlaced = 0
        while (minesPlaced < 3) {
            val row = random.nextInt(5)
            val col = random.nextInt(5)
            if (!grid[row][col].hasMine) {
                grid[row][col].hasMine = true
                minesPlaced++
            }
        }
    }

    // NOTE: check if this works for corner cases or if you get index issues
    fun getNumOfAdjacentMines(row: Int, col: Int) {
        var count = 0
        for (r in row - 1 .. row + 1) {
            for (c in col - 1 .. col + 1) {
                if ((r in 0..4 && c in 0..4) && (r != row || c != col)) {
                    if (grid[r][c].hasMine) {
                        count++
                    }
                }
            }
        }
        grid[row][col].adjacentMines = count
    }

    // NOTE: the game logic-> you only win if everything is flagged correctly, not if all correct cells are revealed. FIX
    fun revealCell(row: Int, col: Int) {
        val cell = grid[row][col]
        if (cell.hasMine) {
            gameState = GameState.LOSE
        } else {
            cell.isRevealed = true
        }
        if (checkWin()) {
            gameState = GameState.WIN
        }
    }

    // NOTE: verify that this code limits to 3 flags
    fun flagCell(row: Int, col: Int) {
        val cell = grid[row][col]
        if (!cell.isRevealed) {
            cell.hasFlag = !cell.hasFlag
            if (cell.hasFlag) {
                remainingFlags--
            } else {
                remainingFlags++
            }
        }
        if (checkWin()) {
            gameState = GameState.WIN
        }
    }

//    fun onCellClicked(row: Any?, col: Int) {
//        if (gameState != GameState.PLAY || grid[row][col].isRevealed || grid[row][col].hasFlag) return
//
//        if (gameMode == GameMode.TRY) {
//            revealCell(row,col)
//        } else {
//            flagCell(row,col)
//        }
//    }

    fun onCellClicked(cell: Cell) {
        if (gameState != GameState.PLAY || grid[cell.row][cell.col].isRevealed || grid[cell.row][cell.col].hasFlag) return

        if (gameMode == GameMode.TRY) {
            revealCell(cell.row,cell.col)
        } else {
            flagCell(cell.row,cell.col)
        }
    }

    // NOTE: the game logic-> you only win if everything is flagged correctly, not if all correct cells are revealed. FIX
    fun checkWin(): Boolean {
        var flagCounter = 0
        var mineCounter = 0
        for (row in 0..5) {
            for (col in 0 .. 5) {
                val cell = grid[row] [col]
                if (cell.hasMine && cell.hasFlag) {
                    flagCounter++
                }
                if (cell.hasMine) {
                    mineCounter++
                }
            }
        }
        return flagCounter == mineCounter
    }

    fun resetGame() {
        grid = Array(5) { Array(5) { Cell(0, 0, false, false, 0 , hasFlag = false) } }
        placeMines()
        for (i in 0..4) {
            for (j in 0..4) {
                getNumOfAdjacentMines(i,j)
            }
        }
        gameMode = GameMode.TRY
        gameState = GameState.PLAY
        remainingFlags = 3
        remainingMines = 3
    }

}

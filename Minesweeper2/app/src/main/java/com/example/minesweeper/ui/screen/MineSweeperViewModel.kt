/*package com.example.minesweeper.ui.screen

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

class MineSweeperViewModel : ViewModel() {
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


 */
package com.example.minesweeper.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.LinkedList
import java.util.Queue

data class Field(
    var type: FieldType, var minesAround: Int, var wasClicked: Boolean
)

data class BoardCell(
    var row: Int, var col: Int
)

enum class GameStatus {
    Playing, FlagLost, BombLost, Win
}

enum class FieldType {
    Bomb, Flag, Empty, Number
}

class MineSweeperViewModel : ViewModel() {
    var boardSize by mutableStateOf(5)
    var flagsPlaced by mutableStateOf(0)
    var bombNumber by mutableStateOf(0)
    var gameStatus by mutableStateOf(null as GameStatus?)
    var instructionText by mutableStateOf("")
    var flagMode by mutableStateOf(false)
    private var bombLocations by mutableStateOf(Array(bombNumber) { null as BoardCell? })

    var board by mutableStateOf(Array(boardSize) {
        Array(boardSize) {
            Field(FieldType.Empty, 0, false)
        }
    })

    init {
        initializeBoard()
    }

    fun initializeBoard() {
        board = Array(boardSize) {
            Array(boardSize) {
                Field(FieldType.Empty, 0, false)
            }
        }
        bombNumber = boardSize - 2
        bombLocations = Array(bombNumber) { null as BoardCell? }
        gameStatus = GameStatus.Playing
        instructionText = ""
        flagsPlaced = 0
        placeBombs(bombNumber)
    }

    fun cellClicked(cell: BoardCell) {
        if (flagMode) {
            onFlagClick(cell)
        } else {
            onCellClicked(cell)
        }
    }

    fun onCellClicked(cell: BoardCell) {
        if (!(board[cell.row][cell.col].wasClicked) && gameStatus == GameStatus.Playing) {
            if (board[cell.row][cell.col].type == FieldType.Bomb) {
                revealBombs()
                gameStatus = GameStatus.BombLost
            } else {
                val newBoard = board.copyOf()
                revealCells(cell)
                board = newBoard
            }
        }
    }

    fun onFlagClick(cell: BoardCell) {
        if (!(board[cell.row][cell.col].wasClicked) && gameStatus == GameStatus.Playing) {
            if (board[cell.row][cell.col].type == FieldType.Bomb) {
                val newBoard = board.copyOf()
                newBoard[cell.row][cell.col].type = FieldType.Flag
                newBoard[cell.row][cell.col].wasClicked = true
                board = newBoard
                flagsPlaced += 1
                if (flagsPlaced == bombNumber) {
                    gameStatus = GameStatus.Win
                }
            } else {
                gameStatus = GameStatus.FlagLost
            }
        }
    }

    fun revealBombs() {
        val newBoard = board.copyOf()
        for (bomb in bombLocations) {
            if (bomb != null) {
                newBoard[bomb.row][bomb.col].type = FieldType.Bomb
                newBoard[bomb.row][bomb.col].wasClicked = true
            }
        }
        board = newBoard
    }

    // Also handles revealing connected empty cells
    fun revealCells(startCell: BoardCell) {
        val queue: Queue<BoardCell> = LinkedList()
        val visited = mutableSetOf<BoardCell>()

        queue.offer(startCell)
        visited.add(startCell)

        while (queue.isNotEmpty()) {
            val currentCell = queue.poll()
            val row = currentCell.row
            val col = currentCell.col

            board[row][col].wasClicked = true

            if (board[row][col].minesAround > 0) {
                continue
            }

            val neighbors = getNeighbors(currentCell)
            for (neighbor in neighbors) {
                if (neighbor !in visited) {
                    visited.add(neighbor)
                    queue.offer(neighbor)
                }
            }
        }
    }


    fun placeBombs(bombNumber: Int) {
        val directions = arrayOf(
            Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0),
            Pair(1, 1), Pair(1, -1), Pair(-1, -1), Pair(-1, 1)
        )
        var count = bombNumber
        while (count > 0) {
            val randomX = (0..<boardSize).random()
            val randomY = (0..<boardSize).random()
            if (board[randomX][randomY].type == FieldType.Empty) {
                board[randomX][randomY].type = FieldType.Bomb
                bombLocations += BoardCell(randomX, randomY)
                for (neighbor in getNeighbors(BoardCell(randomX, randomY))) {
                    if (board[neighbor.row][neighbor.col].type != FieldType.Bomb &&
                        board[neighbor.row][neighbor.col].type != FieldType.Flag
                    ) {
                        board[neighbor.row][neighbor.col].type = FieldType.Number
                        board[neighbor.row][neighbor.col].minesAround += 1
                    }
                }
                count -= 1
            }
        }
    }

    fun getNeighbors(cell: BoardCell): List<BoardCell> {
        val neighbors = mutableListOf<BoardCell>()
        val directions = arrayOf(
            Pair(-1, -1), Pair(-1, 0), Pair(-1, 1), Pair(0, -1),
            Pair(0, 1), Pair(1, -1), Pair(1, 0), Pair(1, 1)
        )

        for (dir in directions) {
            val newRow = cell.row + dir.first
            val newCol = cell.col + dir.second

            if (newRow in 0 until boardSize && newCol in 0 until boardSize) {
                neighbors.add(BoardCell(newRow, newCol))
            }
        }
        return neighbors
    }
}






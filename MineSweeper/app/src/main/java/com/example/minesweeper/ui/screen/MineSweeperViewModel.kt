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






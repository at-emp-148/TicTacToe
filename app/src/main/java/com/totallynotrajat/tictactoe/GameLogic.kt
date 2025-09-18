package com.totallynotrajat.tictactoe

data class GameState(
    val board: List<List<Player?>> = List(3) { List(3) { null } },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val isDraw: Boolean = false,
    val isGameOver: Boolean = false,
    val xWins: Int = 0,
    val oWins: Int = 0,
    val gameStarted: Boolean = false
)

enum class Player {
    X, O;
    
    fun opposite(): Player = if (this == X) O else X
    
    fun symbol(): String = if (this == X) "X" else "O"
}

object GameLogic {
    fun makeMove(gameState: GameState, row: Int, col: Int): GameState {
        if (gameState.isGameOver || gameState.board[row][col] != null || !gameState.gameStarted) {
            return gameState
        }
        
        val newBoard = gameState.board.mapIndexed { r, rowList ->
            rowList.mapIndexed { c, cell ->
                if (r == row && c == col) gameState.currentPlayer else cell
            }
        }
        
        val winner = checkWinner(newBoard)
        val isDraw = winner == null && newBoard.all { row -> row.all { cell -> cell != null } }
        val isGameOver = winner != null || isDraw
        
        return gameState.copy(
            board = newBoard,
            currentPlayer = if (isGameOver) gameState.currentPlayer else gameState.currentPlayer.opposite(),
            winner = winner,
            isDraw = isDraw,
            isGameOver = isGameOver,
            xWins = if (winner == Player.X) gameState.xWins + 1 else gameState.xWins,
            oWins = if (winner == Player.O) gameState.oWins + 1 else gameState.oWins
        )
    }
    
    fun startNewGame(gameState: GameState): GameState {
        return gameState.copy(
            board = List(3) { List(3) { null } },
            currentPlayer = Player.X,
            winner = null,
            isDraw = false,
            isGameOver = false,
            gameStarted = true
        )
    }
    
    fun startNewSession(): GameState {
        return GameState(gameStarted = true)
    }
    
    private fun checkWinner(board: List<List<Player?>>): Player? {
        // Check rows
        for (row in board) {
            if (row.all { it != null && it == row[0] }) {
                return row[0]
            }
        }
        
        // Check columns
        for (col in 0..2) {
            if (board.all { it[col] != null && it[col] == board[0][col] }) {
                return board[0][col]
            }
        }
        
        // Check diagonals
        if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0]
        }
        
        if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2]
        }
        
        return null
    }
}
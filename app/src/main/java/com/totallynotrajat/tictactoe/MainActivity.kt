package com.totallynotrajat.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.totallynotrajat.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeApp()
                }
            }
        }
    }
}

@Composable
fun TicTacToeApp() {
    var gameState by remember { mutableStateOf(GameState()) }
    
    if (!gameState.gameStarted) {
        StartScreen(
            onStartGame = {
                gameState = GameLogic.startNewSession()
            }
        )
    } else {
        GameScreen(
            gameState = gameState,
            onCellClick = { row, col ->
                gameState = GameLogic.makeMove(gameState, row, col)
            },
            onNewGame = {
                gameState = GameLogic.startNewGame(gameState)
            },
            onBackToStart = {
                gameState = GameState()
            }
        )
    }
}
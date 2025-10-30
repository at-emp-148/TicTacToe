package com.totallynotrajat.tictactoe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.totallynotrajat.tictactoe.ui.theme.TicTacToeTheme
import com.trackier.sdk.TrackierSDK
import com.trackier.sdk.TrackierEvent
import com.trackier.sdk.TrackierSDKConfig

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
                val event = TrackierEvent(TrackierEvent.LOGIN)

                /* Below are the function for the adding the extra data,
                   You can add the extra data like login details of user or anything you need.
                   We have 10 params to add data, Below 5 are mentioned */
                event.param1 = "This 1"
                event.param2 = "was 2"
                event.param3 = "a 3"
                event.param4 = "test 4"
                event.param5 = "Android 5"
                TrackierSDK.trackEvent(event)
                Log.d("TAG", "onClick: event_track ")
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
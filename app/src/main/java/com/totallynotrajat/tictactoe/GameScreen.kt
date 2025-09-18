package com.totallynotrajat.tictactoe

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    gameState: GameState,
    onCellClick: (Int, Int) -> Unit,
    onNewGame: () -> Unit,
    onBackToStart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1C1C1E),
                        Color(0xFF000000)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Score display
            ScoreCard(
                xWins = gameState.xWins,
                oWins = gameState.oWins
            )
            
            // Current player indicator
            if (!gameState.isGameOver) {
                CurrentPlayerIndicator(currentPlayer = gameState.currentPlayer)
            }
            
            // Game board
            GameBoard(
                board = gameState.board,
                onCellClick = onCellClick,
                isGameOver = gameState.isGameOver
            )
            
            // Game status and controls
            if (gameState.isGameOver) {
                GameOverDisplay(
                    winner = gameState.winner,
                    isDraw = gameState.isDraw,
                    onNewGame = onNewGame,
                    onBackToStart = onBackToStart
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Back to start button
            if (!gameState.isGameOver) {
                OutlinedButton(
                    onClick = onBackToStart,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFAAAAAA)
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFAAAAAA), Color(0xFFAAAAAA))
                        )
                    )
                ) {
                    Text("Back to Start")
                }
            }
        }
    }
}

@Composable
fun ScoreCard(
    xWins: Int,
    oWins: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Player X",
                    color = Color(0xFF00C7FF),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "$xWins",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "wins",
                    color = Color(0xFFAAAAAA),
                    fontSize = 12.sp
                )
            }
            
            Divider(
                modifier = Modifier
                    .height(60.dp)
                    .width(1.dp),
                color = Color(0xFF3A3A3C)
            )
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Player O",
                    color = Color(0xFFFF6B6B),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "$oWins",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "wins",
                    color = Color(0xFFAAAAAA),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun CurrentPlayerIndicator(currentPlayer: Player) {
    val infiniteTransition = rememberInfiniteTransition(label = "playerIndicator")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Text(
        text = "Player ${currentPlayer.symbol()}'s Turn",
        color = if (currentPlayer == Player.X) 
            Color(0xFF00C7FF).copy(alpha = alpha) 
        else 
            Color(0xFFFF6B6B).copy(alpha = alpha),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun GameBoard(
    board: List<List<Player?>>,
    onCellClick: (Int, Int) -> Unit,
    isGameOver: Boolean
) {
    Card(
        modifier = Modifier.padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            board.forEachIndexed { rowIndex, row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEachIndexed { colIndex, cell ->
                        GameCell(
                            player = cell,
                            onClick = { onCellClick(rowIndex, colIndex) },
                            isClickable = cell == null && !isGameOver
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameCell(
    player: Player?,
    onClick: () -> Unit,
    isClickable: Boolean
) {
    var hasAnimated by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (player != null && !hasAnimated) 0f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cellScale"
    )
    
    LaunchedEffect(player) {
        if (player != null && !hasAnimated) {
            delay(50)
            hasAnimated = true
        }
    }
    
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(
                if (isClickable) Color(0xFF3A3A3C) else Color(0xFF48484A),
                RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (isClickable) Modifier.clickable { onClick() } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (player != null) {
            Text(
                text = player.symbol(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = if (player == Player.X) Color(0xFF00C7FF) else Color(0xFFFF6B6B),
                modifier = Modifier.scale(scale)
            )
        }
    }
}

@Composable
fun GameOverDisplay(
    winner: Player?,
    isDraw: Boolean,
    onNewGame: () -> Unit,
    onBackToStart: () -> Unit
) {
    var showAnimation by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        showAnimation = true
    }
    
    AnimatedVisibility(
        visible = showAnimation,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Victory/Draw message with celebration animation
            VictoryMessage(winner = winner, isDraw = isDraw)
            
            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onNewGame,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00C7FF),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Play Again", fontWeight = FontWeight.Bold)
                }
                
                OutlinedButton(
                    onClick = onBackToStart,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFAAAAAA)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Back to Start")
                }
            }
        }
    }
}

@Composable
fun VictoryMessage(
    winner: Player?,
    isDraw: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "victory")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "victoryScale"
    )
    
    val message = when {
        isDraw -> "It's a Draw!"
        winner == Player.X -> "Player X Wins!"
        winner == Player.O -> "Player O Wins!"
        else -> ""
    }
    
    val color = when {
        isDraw -> Color(0xFFFFC107)
        winner == Player.X -> Color(0xFF00C7FF)
        winner == Player.O -> Color(0xFFFF6B6B)
        else -> Color.White
    }
    
    Text(
        text = message,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        textAlign = TextAlign.Center,
        modifier = Modifier.scale(scale)
    )
}
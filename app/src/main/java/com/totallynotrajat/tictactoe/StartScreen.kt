package com.totallynotrajat.tictactoe

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun StartScreen(
    onStartGame: () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (showContent) 1f else 0.8f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "scale"
    )
    
    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }
    
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
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .scale(scale)
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Title with animated appearance
            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(800, delayMillis = 200)
                ) + fadeIn(tween(800, delayMillis = 200))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "TIC TAC TOE",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Get ready for an epic battle!",
                        fontSize = 16.sp,
                        color = Color(0xFFAAAAAA),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Animated game preview
            AnimatedVisibility(
                visible = showContent,
                enter = scaleIn(
                    animationSpec = tween(600, delayMillis = 600)
                ) + fadeIn(tween(600, delayMillis = 600))
            ) {
                PreviewBoard()
            }
            
            // Start button with pulse animation
            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(800, delayMillis = 800)
                ) + fadeIn(tween(800, delayMillis = 800))
            ) {
                StartGameButton(onStartGame = onStartGame)
            }
        }
    }
}

@Composable
fun PreviewBoard() {
    val previewBoard = listOf(
        listOf(Player.X, null, Player.O),
        listOf(null, Player.X, null),
        listOf(Player.O, null, Player.X)
    )
    
    Card(
        modifier = Modifier.size(200.dp),
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
            previewBoard.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { cell ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Color(0xFF3A3A3C),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (cell != null) {
                                Text(
                                    text = cell.symbol(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (cell == Player.X) Color(0xFF00C7FF) else Color(0xFFFF6B6B)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StartGameButton(onStartGame: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "buttonScale"
    )
    
    // Infinite pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    
    Button(
        onClick = onStartGame,
        modifier = Modifier
            .scale(scale * pulseScale)
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00C7FF),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(28.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp
        )
    ) {
        Text(
            text = "START GAME",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
package com.totallynotrajat.tictactoe

import org.junit.Test
import org.junit.Assert.*

class GameLogicTest {

    @Test
    fun testInitialGameState() {
        val gameState = GameState()
        assertFalse(gameState.gameStarted)
        assertEquals(Player.X, gameState.currentPlayer)
        assertNull(gameState.winner)
        assertFalse(gameState.isDraw)
        assertFalse(gameState.isGameOver)
        assertEquals(0, gameState.xWins)
        assertEquals(0, gameState.oWins)
    }

    @Test
    fun testStartNewSession() {
        val gameState = GameLogic.startNewSession()
        assertTrue(gameState.gameStarted)
        assertEquals(Player.X, gameState.currentPlayer)
        assertNull(gameState.winner)
        assertFalse(gameState.isDraw)
        assertFalse(gameState.isGameOver)
    }

    @Test
    fun testMakeMove() {
        val gameState = GameLogic.startNewSession()
        val newState = GameLogic.makeMove(gameState, 0, 0)
        
        assertEquals(Player.X, newState.board[0][0])
        assertEquals(Player.O, newState.currentPlayer)
        assertFalse(newState.isGameOver)
    }

    @Test
    fun testWinDetection() {
        val gameState = GameLogic.startNewSession()
        
        // Create winning scenario for X (first row)
        var state = GameLogic.makeMove(gameState, 0, 0) // X
        state = GameLogic.makeMove(state, 1, 0) // O
        state = GameLogic.makeMove(state, 0, 1) // X
        state = GameLogic.makeMove(state, 1, 1) // O
        state = GameLogic.makeMove(state, 0, 2) // X wins
        
        assertEquals(Player.X, state.winner)
        assertTrue(state.isGameOver)
        assertEquals(1, state.xWins)
    }

    @Test
    fun testDrawDetection() {
        val gameState = GameLogic.startNewSession()
        
        // Create a draw scenario
        var state = GameLogic.makeMove(gameState, 0, 0) // X
        state = GameLogic.makeMove(state, 0, 1) // O
        state = GameLogic.makeMove(state, 0, 2) // X
        state = GameLogic.makeMove(state, 1, 0) // O
        state = GameLogic.makeMove(state, 1, 1) // X
        state = GameLogic.makeMove(state, 2, 0) // O
        state = GameLogic.makeMove(state, 1, 2) // X
        state = GameLogic.makeMove(state, 2, 2) // O
        state = GameLogic.makeMove(state, 2, 1) // X
        
        assertTrue(state.isDraw)
        assertTrue(state.isGameOver)
        assertNull(state.winner)
    }

    @Test
    fun testCannotMoveOnOccupiedCell() {
        val gameState = GameLogic.startNewSession()
        val state1 = GameLogic.makeMove(gameState, 0, 0) // X
        val state2 = GameLogic.makeMove(state1, 0, 0) // Try to move on same cell
        
        // State should not change
        assertEquals(state1.board, state2.board)
        assertEquals(state1.currentPlayer, state2.currentPlayer)
    }

    @Test
    fun testCannotMoveAfterGameOver() {
        val gameState = GameLogic.startNewSession()
        
        // Create winning scenario
        var state = GameLogic.makeMove(gameState, 0, 0) // X
        state = GameLogic.makeMove(state, 1, 0) // O
        state = GameLogic.makeMove(state, 0, 1) // X
        state = GameLogic.makeMove(state, 1, 1) // O
        state = GameLogic.makeMove(state, 0, 2) // X wins
        
        val finalState = GameLogic.makeMove(state, 2, 2) // Try to move after win
        
        // State should not change
        assertEquals(state.board, finalState.board)
        assertTrue(finalState.isGameOver)
    }
}
package com.example.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class ZigzagGameTest {

    @Test
    @DisplayName("Should create game with valid parameters")
    void createGameWithValidParameters() {
        ZigzagGame game = new ZigzagGame(10, 5);
        assertNotNull(game);
        assertEquals(new ZigzagGame.Position(0, 0), game.getCurrentPosition());
        assertEquals(ZigzagGame.Direction.RIGHT, game.getCurrentDirection());
        assertEquals(0, game.getStepCount());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Should throw exception for invalid steps")
    void throwExceptionForInvalidSteps(int steps) {
        assertThrows(IllegalArgumentException.class, () -> new ZigzagGame(steps, 5));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 0, -1})
    @DisplayName("Should throw exception for invalid width")
    void throwExceptionForInvalidWidth(int width) {
        assertThrows(IllegalArgumentException.class, () -> new ZigzagGame(10, width));
    }

    @Test
    @DisplayName("Should move right initially")
    void moveRightInitially() throws InterruptedException {
        ZigzagGame game = new ZigzagGame(5, 3);
        game.play();
        assertTrue(game.getCurrentPosition().x() > 0);
        assertEquals(ZigzagGame.Direction.LEFT, game.getCurrentDirection());
    }

    @Test
    @DisplayName("Should change direction at boundaries")
    void changeDirectionAtBoundaries() throws InterruptedException {
        ZigzagGame game = new ZigzagGame(10, 3);
        game.play();
        assertTrue(game.getCurrentPosition().x() >= 0 && game.getCurrentPosition().x() < 3);
    }

    @ParameterizedTest
    @CsvSource({
        "5, 3, 2",
        "10, 4, 3",
        "15, 5, 4"
    })
    @DisplayName("Should respect game boundaries")
    void respectGameBoundaries(int steps, int width, int expectedMaxY) throws InterruptedException {
        ZigzagGame game = new ZigzagGame(steps, width);
        ZigzagGame.Position finalPosition = game.play();
        assertTrue(finalPosition.y() <= expectedMaxY);
    }

    @Test
    @DisplayName("Should maintain valid position throughout game")
    void maintainValidPosition() throws InterruptedException {
        ZigzagGame game = new ZigzagGame(20, 5);
        game.play();
        ZigzagGame.Position position = game.getCurrentPosition();
        assertTrue(position.x() >= 0 && position.x() < 5);
        assertTrue(position.y() >= 0);
    }

    @Test
    @DisplayName("Should have consistent state after game over")
    void consistentStateAfterGameOver() throws InterruptedException {
        ZigzagGame game = new ZigzagGame(10, 4);
        ZigzagGame.Position finalPosition = game.play();
        assertEquals(finalPosition, game.getCurrentPosition());
        assertTrue(game.getStepCount() <= 10);
    }

    @Test
    @DisplayName("Should handle interruption gracefully")
    void handleInterruption() {
        ZigzagGame game = new ZigzagGame(100, 5);
        Thread.currentThread().interrupt();
        assertThrows(InterruptedException.class, game::play);
    }
} 
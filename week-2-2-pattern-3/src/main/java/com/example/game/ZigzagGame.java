package com.example.game;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Modern implementation of a Zigzag movement game with improved features and safety.
 */
public class ZigzagGame {
    private static final Logger LOGGER = Logger.getLogger(ZigzagGame.class.getName());
    private static final Duration STEP_DELAY = Duration.ofMillis(200);
    private static final int MIN_WIDTH = 2;
    private static final int MIN_STEPS = 1;

    private final int steps;
    private final int width;
    private Position currentPosition;
    private Direction currentDirection;
    private int stepCount;

    /**
     * Record representing a 2D position
     */
    public record Position(int x, int y) {
        public Position {
            if (x < 0 || y < 0) {
                throw new IllegalArgumentException("Position coordinates cannot be negative");
            }
        }
    }

    /**
     * Enum representing movement direction
     */
    public enum Direction {
        RIGHT(true),
        LEFT(false);

        private final boolean isRight;

        Direction(boolean isRight) {
            this.isRight = isRight;
        }

        public boolean isRight() {
            return isRight;
        }
    }

    /**
     * Creates a new ZigzagGame instance
     * @param steps Total number of steps to move
     * @param width Width of the movement area
     * @throws IllegalArgumentException if steps or width are invalid
     */
    public ZigzagGame(int steps, int width) {
        validateParameters(steps, width);
        this.steps = steps;
        this.width = width;
        this.currentPosition = new Position(0, 0);
        this.currentDirection = Direction.RIGHT;
        this.stepCount = 0;
    }

    private void validateParameters(int steps, int width) {
        if (steps < MIN_STEPS) {
            throw new IllegalArgumentException("Steps must be at least " + MIN_STEPS);
        }
        if (width < MIN_WIDTH) {
            throw new IllegalArgumentException("Width must be at least " + MIN_WIDTH);
        }
    }

    /**
     * Executes the zigzag movement game
     * @return final position of the game
     * @throws InterruptedException if the game is interrupted during execution
     */
    public Position play() throws InterruptedException {
        LOGGER.info("Starting Zigzag game with steps: " + steps + ", width: " + width);
        
        while (stepCount < steps && !isGameOver()) {
            move();
            logPosition();
            TimeUnit.MILLISECONDS.sleep(STEP_DELAY.toMillis());
            stepCount++;
        }

        LOGGER.info("Game Over at " + currentPosition);
        return currentPosition;
    }

    private void move() {
        Position newPosition = calculateNextPosition();
        if (isValidPosition(newPosition)) {
            currentPosition = newPosition;
            updateDirection();
        }
    }

    private Position calculateNextPosition() {
        return currentDirection.isRight() 
            ? new Position(currentPosition.x() + 1, currentPosition.y())
            : new Position(currentPosition.x() - 1, currentPosition.y());
    }

    private boolean isValidPosition(Position position) {
        return position.x() >= 0 && position.x() < width;
    }

    private void updateDirection() {
        if (currentPosition.x() == width - 1) {
            currentDirection = Direction.LEFT;
            currentPosition = new Position(currentPosition.x(), currentPosition.y() + 1);
        } else if (currentPosition.x() == 0) {
            currentDirection = Direction.RIGHT;
            currentPosition = new Position(currentPosition.x(), currentPosition.y() + 1);
        }
    }

    private boolean isGameOver() {
        return currentPosition.y() > steps / 2;
    }

    private void logPosition() {
        LOGGER.info(String.format("Step %d: %s", stepCount + 1, currentPosition));
    }

    /**
     * Gets the current position
     * @return current position
     */
    public Position getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Gets the current direction
     * @return current direction
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Gets the current step count
     * @return current step count
     */
    public int getStepCount() {
        return stepCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZigzagGame that = (ZigzagGame) o;
        return steps == that.steps && 
               width == that.width && 
               stepCount == that.stepCount && 
               Objects.equals(currentPosition, that.currentPosition) && 
               currentDirection == that.currentDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(steps, width, currentPosition, currentDirection, stepCount);
    }

    @Override
    public String toString() {
        return String.format("ZigzagGame{steps=%d, width=%d, currentPosition=%s, currentDirection=%s, stepCount=%d}",
                steps, width, currentPosition, currentDirection, stepCount);
    }
} 
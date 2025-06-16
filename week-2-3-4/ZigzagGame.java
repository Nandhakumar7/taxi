import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A class that simulates a zigzag movement game where an object moves horizontally
 * back and forth while incrementing vertically until reaching a maximum number of steps.
 */
public class ZigzagGame {
    private final int steps;
    private final int width;
    private int x;
    private int y;
    private boolean movingRight;
    private int currentStep;
    private final ScheduledExecutorService scheduler;

    /**
     * Constructs a new ZigzagGame instance.
     *
     * @param steps The maximum number of steps to move
     * @param width The width of the movement area
     * @throws IllegalArgumentException if steps is less than or equal to 0 or width is less than or equal to 1
     */
    public ZigzagGame(int steps, int width) {
        if (steps <= 0 || width <= 1) {
            throw new IllegalArgumentException("Invalid input: steps must be positive and width must be greater than 1");
        }
        this.steps = steps;
        this.width = width;
        this.x = 0;
        this.y = 0;
        this.movingRight = true;
        this.currentStep = 0;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Starts the zigzag game simulation.
     * The game will automatically stop when either:
     * - The maximum number of steps is reached
     * - The vertical position exceeds half of the total steps
     */
    public void start() {
        move();
    }

    /**
     * Stops the game and cleans up resources.
     */
    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Executes a single move in the game.
     * This method is called recursively with a delay to simulate movement.
     */
    private void move() {
        if (currentStep >= steps || y > steps / 2) {
            System.out.printf("Game Over at (%d, %d)%n", x, y);
            stop();
            return;
        }

        System.out.printf("Step %d: (%d, %d)%n", currentStep + 1, x, y);
        currentStep++;

        if (movingRight) {
            x++;
            if (x == width - 1) {
                y++;
                movingRight = false;
            }
        } else {
            x--;
            if (x == 0) {
                y++;
                movingRight = true;
            }
        }

        // Schedule the next move with a delay
        scheduler.schedule(this::move, 200, TimeUnit.MILLISECONDS);
    }

    /**
     * Main method to demonstrate the ZigzagGame functionality.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            ZigzagGame game = new ZigzagGame(10, 5);
            game.start();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
} 
function zigzagGame(steps, width) {
    if (steps <= 0 || width <= 1) {
        console.log("Invalid input");
        return;
    }

    let x = 0, y = 0;
    let right = true;

    let i = 0;

    function move() {
        if (i >= steps || y > Math.floor(steps / 2)) {
            console.log(`Game Over at (${x}, ${y})`);
            return;
        }

        console.log(`Step ${i + 1}: (${x}, ${y})`);
        i++;

        if (right) {
            x++;
            if (x === width - 1) {
                y++;
                right = false;
            }
        } else {
            x--;
            if (x === 0) {
                y++;
                right = true;
            }
        }

        setTimeout(move, 200); // simulate delay
    }

    move();
}
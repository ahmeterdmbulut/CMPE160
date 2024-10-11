/**
 * A game where player tries to hit the targets
 * @author Ahmet Erdem Bulut, Student ID: 2022400093
 * @since Date: 17.03.2024
 */

import java.awt.event.KeyEvent;

public class AhmetErdemBulut {

    /**
     * Defines the necessary parameters and runs the main game.
     * @param args Main input arguments are not used
     */
    public static void main(String[] args) {
        // Game Parameters
        int width = 1600; //screen width
        int height = 800; // screen height
        double gravity = 9.80665; // gravity
        double x0 = 120; // x and y coordinates of the bullet’s starting position on the platform
        double y0 = 120;
        double bulletVelocity = 180; // initial velocity
        double bulletAngle = 45.0; // initial angle
        // Box coordinates for obstacles and targets
        // Each row stores a box containing the following information:
        // x and y coordinates of the lower left rectangle corner, width, and height
        double[][] obstacleArray = {
                {1200, 0, 60, 220},
                {1000, 0, 60, 160},
                {600, 0, 60, 80},
                {600, 180, 60, 160},
                {220, 0, 120, 180}
        };
        double[][] targetArray = {
                {1160, 0, 30, 30},
                {730, 0, 30, 30},
                {150, 0, 20, 20},
                {1480, 0, 60, 60},
                {340, 80, 60, 30},
                {1500, 600, 60, 60}
        };

        // my obstacles and targets
//        double[][] obstacleArray = {
//                {340, 0, 160, 200},
//                {420, 500, 60, 80},
//                {250, 350, 60, 80},
//                {750, 0, 60, 180},
//                {1100, 0, 70, 500},
//                {810, 400, 120, 45}
//        };
//        double[][] targetArray = {
//                {250, 30, 30, 30},
//                {600, 480, 40, 40},
//                {600, 190, 40, 40},
//                {870, 0, 50, 50},
//                {960, 200, 40, 40},
//                {1300, 450, 80, 80},
//                {1500, 190, 50, 60}
//        };

        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(0, width); // x coordinate scale
        StdDraw.setYscale(0, height); // y coordinate scale
        StdDraw.enableDoubleBuffering(); // for smooth animations

        setCanvas(x0, y0, bulletAngle, bulletVelocity, obstacleArray, targetArray); // drawing the targets, obstacles and shooting platform

        boolean shootingResult = false; // shooting controller, if the bullet hits somewhere, we will change it to true

        while (true){
            // If shootingResult condition is false, that means either the motion hasn't concluded or the motion hasn't started yet,
            // so while that boolean is true, the velocity and angle values shouldn't be changed.
            // When the player pressed to up arrow, angle value will be incremented, and we will draw the canvas again to update that value
            // Down arrow will decrement the angle value
            // Right arrow will increment the velocity value
            // Left arrow will decrement the velocity value
            // Space key will start the motion
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP) && !shootingResult) {
                bulletAngle++;
                setCanvas(x0, y0, bulletAngle, bulletVelocity, obstacleArray, targetArray);
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN) && !shootingResult) {
                bulletAngle--;
                setCanvas(x0, y0, bulletAngle, bulletVelocity, obstacleArray, targetArray);
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) && !shootingResult) {
                bulletVelocity--;
                setCanvas(x0, y0, bulletAngle, bulletVelocity, obstacleArray, targetArray);
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) && !shootingResult) {
                bulletVelocity++;
                setCanvas(x0, y0, bulletAngle, bulletVelocity, obstacleArray, targetArray);
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE) && !shootingResult) {
                // We only want the motion to start while the game hasn't concluded yet
                drawBulletTrajectory(x0, y0, gravity, bulletVelocity, bulletAngle, targetArray, obstacleArray, width);
                // when bullet hit somewhere we change the shootingResult to true
                shootingResult = true;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_R) && shootingResult) {
                // When the game finishes, R key should reset the canvas and start the game again with default values
                shootingResult = false;
                resetCanvas(x0, y0, obstacleArray, targetArray);
                bulletAngle = 45;
                bulletVelocity = 180;
            }
            StdDraw.show();
            StdDraw.pause(100);
        }

    }

    /**
     * Draws the game environment. Shooting platform, targets and obstacles
     * @param x0 initial x coordinate of the bullet and upper right corner of the shooting platform
     * @param y0 initial y coordinate of the bullet and upper right corner of the shooting platform
     * @param bulletAngle initial angle with which bullet will be shot
     * @param bulletVelocity initial velocity with which bullet will be shot
     * @param obstacleArray an array storing the coordinates and lengths of obstacles
     * @param targetArray an array storing the coordinates and lengths of targets
     */
    public static void setCanvas(double x0, double y0,
                          double bulletAngle, double bulletVelocity, double[][] obstacleArray, double[][] targetArray){
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledSquare(x0 / 2, y0 / 2, x0 / 2); // shooting platform
        double x1 = x0 + bulletVelocity * Math.cos(Math.toRadians(bulletAngle)) / 3; // end coordinates of shooting line
        double y1 = y0 + bulletVelocity * Math.sin(Math.toRadians(bulletAngle)) / 3;
        StdDraw.setPenRadius(0.008);
        StdDraw.line(x0, y0, x1, y1); // shooting line
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(60, 60, "a: " + bulletAngle);
        StdDraw.text(60, 40, "v: " + bulletVelocity);
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        for (double[] obstacles : obstacleArray) {
            // drawing the obstacles
            double halfWidth = obstacles[2] / 2;
            double halfHeight = obstacles[3] / 2;
            double centerX = obstacles[0] + halfWidth;
            double centerY = obstacles[1] + halfHeight;
            StdDraw.filledRectangle(centerX, centerY, halfWidth, halfHeight);
        }
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        for (double[] targets : targetArray) {
            // drawing the targets
            double halfWidth = targets[2] / 2;
            double halfHeight = targets[3] / 2;
            double centerX = targets[0] + halfWidth;
            double centerY = targets[1] + halfHeight;
            StdDraw.filledRectangle(centerX, centerY, halfWidth, halfHeight);
        }
    }

    /**
     * Draws the trajectory of bullet.
     * @param x0 initial x coordinate of the bullet
     * @param y0 initial y coordinate of the bullet
     * @param gravity gravity
     * @param bulletVelocity velocity
     * @param bulletAngle angle
     * @param targetArray an array storing the coordinates and lengths of targets
     * @param obstacleArray an array storing the coordinates and lengths of obstacles
     * @param width width of the game environment
     */
    public static void drawBulletTrajectory(double x0, double y0, double gravity, double bulletVelocity,
                                            double bulletAngle, double[][] targetArray, double[][] obstacleArray, int width){
        double time = 0; // initial time
        double x = x0; // initial x coordinate
        double y = y0; // initial y coordinate
        StdDraw.setPenColor(StdDraw.BLACK);
        while (true){ // infinite loop for drawing the trajectory until the bullet hit somewhere
            double x1 = x; // stores the bullet's x coordinate in previous frame
            double y1 = y; // stores the bullet's y coordinate in previous frame
            double vx = bulletVelocity/3 * Math.cos(Math.toRadians(bulletAngle)); // x component of velocity divided by three for the bullet's trajectory to fit in game environment
            double vy = bulletVelocity/3 * Math.sin(Math.toRadians(bulletAngle)); // y component of velocity divided by three for the bullet's trajectory to fit in game environment
            double ay = gravity / 3; // gravity divided by three for the bullet's trajectory to fit in game environment
            x = x0 + vx * time; // updating the x coordinate
            y = y0 + vy * time - ay * time * time / 2; // updating the y coordinate
            StdDraw.filledCircle(x, y, 3); // drawing the bullet
            StdDraw.line(x1, y1, x, y); // drawing the line between previous and new positions of bullet to have a trajectory

            // checking for where the bullet hit
            if (checkTarget(x, y, targetArray)){
                StdDraw.textLeft(10, 775, "Congratulations. You hit the target!");
                break;
            } else if (checkObstacle(x, y, obstacleArray)) {
                StdDraw.textLeft(10, 775, "Hit an obstacle. Press 'r' to shoot again.");
                break;
            } else if (checkGround(y)) {
                StdDraw.textLeft(10, 775, "Hit the ground. Press 'r' to shoot again.");
                break;
            } else if (checkXCoord(x, width)) {
                StdDraw.textLeft(10, 775, "Max X reached. Press 'r' to shoot again.");
                break;
            }
            StdDraw.show();
            StdDraw.pause(20);
            time += 0.3; // time increment for next frame
        }
    }


    /**
     * Checks for if the bullet hit a target.
     * @param x x coordinate of the bullet
     * @param y y coordinate of the bullet
     * @param targetArray an array storing the coordinates and lengths of targets
     * @return true if the bullet hits a target
     */
    public static boolean checkTarget(double x, double y, double[][] targetArray){
        for (double[] target : targetArray) {
            if (x >= target[0] && x <= target[0] + target[2] && y >= target[1] && y <= target[1] + target[3]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for if the bullet hit an obstacle.
     * @param x x coordinate of the bullet
     * @param y y coordinate of the bullet
     * @param obstacleArray an array storing the coordinates and lengths of obstacles
     * @return true if the bullet hit an obstacle
     */
    public static boolean checkObstacle(double x, double y, double[][] obstacleArray){
        for (double[] obstacle : obstacleArray) {
            if (x >= obstacle[0] && x <= obstacle[0] + obstacle[2] && y >= obstacle[1] && y <= obstacle[1] + obstacle[3]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for if the bullet hit the ground.
     * @param y y coordinate of the bullet
     * @return true if the bullet hit the ground
     */
    public static boolean checkGround(double y){
        return y <= 0;
    }

    /**
     * Checks for if the bullet reached or passed the width of the game environment.
     * @param x x coordinate of the bullet
     * @param width width of the game environment
     * @return true if the bullet reaches or passes the width
     */
    public static boolean checkXCoord(double x, int width){
        return x >= width;
    }

    /**
     * Clears the current game environment and draws the default game environment.
     * @param x0 initial x coordinate of the bullet
     * @param y0 initial y coordinate of the bullet
     * @param obstacleArray an array storing the coordinates and lengths of obstacles
     * @param targetArray an array storing the coordinates and lengths of targets
     */
    public static void resetCanvas(double x0, double y0, double[][] obstacleArray, double[][] targetArray){
        setCanvas(x0, y0, 45, 180, obstacleArray, targetArray);
    }

}

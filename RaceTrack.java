/* Program title: Race Track
 * Authors: Simon Amtoft Pedersen & Morten Holmark Vandborg
 *
 * Short program description:
 * Car game with 2 different maps.
 * User controls the cars speed to reach the finish line.
 * Try not to crash!
 */

import java.awt.*;
import java.util.Scanner;
import java.io.File;

public class RaceTrack {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        deleteFiles(); // Deletes
        welcomeMessage();
        int track = getTrackNumber(console);
        chooseTrack(track, console);
    }

    // Deletes all the created PNG files
    private static void deleteFiles() {
        int number = 0;
        for (int i = 0; i <= number; i++) {
            File f = new File(i + "TRACK.PNG");

            if(f.delete()) {
                number++;
            }
        }
    }

    private static void welcomeMessage() {
        System.out.println("Welcome to the Race Track game!\n\n" +
                "Choose one of the following tracks to play on\n" +
                "Track 1: Square track\n" +
                "Track 2: Rectangular track\n");
    }

    private static void exitMessage() {
        System.out.println("\nThanks for playing!\nExiting...");
        deleteFiles(); // Deletes files after playing
        System.exit(0);
    }

    // Makes sure user picks one of the 2 tracks
    private static int getTrackNumber(Scanner console) {
        boolean get = true;
        int track = 0;

        while (get) {
            System.out.print("Enter track number: ");
            while (!console.hasNextInt()) {
                console.next();
                System.out.print("Track number must an integer!" +
                        "\nPlease enter new track number: ");
            }

            track = console.nextInt(); // Track number

            if (track != 1 && track != 2) {
                System.out.println("Please enter one of the track numbers 1 or 2.");
            } else {
                get = false;
            }
        }
        return track;
    }

    // Makes sure user picks an even integer track size between 14 and 32
    private static float getTrackSize(Scanner console) {
        boolean get = true;
        float size = 0;

        // Checks if input is a float value
        while (get) {
            System.out.print("Enter even track size (14 to 32): ");
            while (!console.hasNextInt()) {
                console.next();
                System.out.print("Track size must an integer!" +
                        "\nPlease enter new track size: ");
            }
            size = console.nextFloat(); // Defines user input as 'size'

            // Checks if size is between 14 and 32
            if (size < 14 || size > 32 || size % 2 != 0) {
                System.out.println("Please enter an even track size between 14 and 32 inclusive.");
            } else {
                get = false;
            }
        }
        return size;
    }

    // Makes sure user answers 'yes' or 'no'
    private static String getRetryAnswer(Scanner console) {
        boolean get = true;
        String ans = "";

        // Checks if input is a string
        while (get) {
            System.out.print("\nTry again? (yes/no): ");
            while (!console.hasNext()) {
                console.next();
                System.out.print("Answer must be a string!" +
                        "\nPlease enter new answer:");
            }

            ans = console.next(); // Answer

            // Checks if answer is yes or no.
            if (!ans.equals("yes") && !ans.equals("no")) {
                System.out.println("Answer must be \"yes\" or \"no\": ");
            } else {
                get = false;
            }
        }
        return ans;
    }

    private static void chooseTrack(int track, Scanner console) {
        float size = getTrackSize(console);
        float limY;
        float limX = size;

        // Sets the limit in the y-axis
        if (track == 1) { // Track 1 (square)
            limY = size;
        } else {          // Track 2 (rectangle)
            limY = size / 2;

        }

        drawTrack(limX,limY,track);    // Draws track
        controlRaceCar((int) limX, (int) limY,
                console, track, size); // Loads control of the Race Car
    }

    private static void controlRaceCar(int limX, int limY, Scanner console, int track, float size) {
        // Variables used to determine when user crosses the finish line
        boolean checkpoint = false;
        boolean clockwise = false;

        // Start player car variables
        int moves = 0;                                         // Number of user move
        Point pos = new Point(limX / 2, 11 * limY / 12); // Position
        Point speed = new Point(0, 0);                   // Speed

        // Draw and save first picture
        StdDraw.save("0TRACK.PNG");  // Save drawing
        drawPlayerCar(limX, pos, speed);     // Draw start position car

        while (true) {
            System.out.print("Enter control integer from 1 to 9 (Press 0 to terminate): ");
            int control = console.nextInt(); // User control

            // Speed control of car
            switch (control) {
                case 1:
                    speed.x -= 1;
                    speed.y -= 1;
                    break;
                case 2:
                    speed.y -= 1;
                    break;
                case 3:
                    speed.x += 1;
                    speed.y -= 1;
                    break;
                case 4:
                    speed.x -= 1;
                    break;
                case 5:
                    break; // Do nothing
                case 6:
                    speed.x += 1;
                    break;
                case 7:
                    speed.x -= 1;
                    speed.y += 1;
                    break;
                case 8:
                    speed.y += 1;
                    break;
                case 9:
                    speed.x += 1;
                    speed.y += 1;
                    break;
                case 0: // User exit
                    exitMessage();
                default: // Prints message if not one of the 10 controls
                    System.out.println("Number has to be an integer from 1 to 9! (or 0 to exit)");
                    break;
            }

            // Checks if the car crosses the finish line
            clockwise = detectClockwise(clockwise, checkpoint, pos, limX);
            checkpoint = detectCheckpoint(checkpoint, pos, limX, limY);
            detectRaceFinish(moves, limX, limY, pos, clockwise, checkpoint);

            // Change position of car and draws new position
            pos.translate(speed.x, speed.y);                 // Changes car position
            moves++;                                         // Count total user moves
            drawCarMovement(moves, limX, limY, pos, speed);  // Draws and speed


            // Resets user speed and position if crashed
            if (detectUserCrash(limX, limY, pos, console, track)) {
                pos.x = limX / 2;
                pos.y = 11 * limY / 12;
                speed.x = 0;
                speed.y = 0;
                moves = 0;
                drawCarMovement(1, limX, limY, pos, speed);
            }
        }
    }

    private static void drawCarMovement(int moves, float limX, float limY, Point pos, Point speed) {
        StdDraw.picture(limX / 2, limY / 2, moves - 1 + "TRACK.PNG");
        drawLine(limX, pos, speed);                                         // Draw line between old and new position
        StdDraw.save(moves + "TRACK.PNG");                         // Saves image without car
        StdDraw.clear();                                                    // Clears canvas
        StdDraw.picture(limX / 2, limY / 2, moves + "TRACK.PNG"); // Loads image
        drawPlayerCar(limX, pos, speed);                                   // Draws car
        drawSpeed(speed, limX, limY);                                      // Draws user speed
    }

    private static void drawPlayerCar(float limX, Point pos, Point speed) {
        double carSize = limX / 18.;                                        // Size of car
        double angle = 270 + Math.toDegrees(Math.atan2(speed.y, speed.x));  // Orientation of car
        StdDraw.picture(pos.x, pos.y, "playerCar.PNG", carSize * 0.7, carSize, angle);
    }

    // Draws line from old to new position
    private static void drawLine(float limX, Point pos, Point speed) {
        StdDraw.setPenRadius(limX / 4000.);             // Line size
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);   // Line color
        StdDraw.line(pos.x, pos.y, pos.x - speed.x,
                pos.y - speed.y);                   // Draw Line
    }

    private static void drawSpeed(Point speed, float limX, float limY) {
        // Defines look of text
        StdDraw.setPenColor(StdDraw.BLACK);
        Font font = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(font);

        // Draws new speed
        StdDraw.text(limX / 2, limY / 2, ("Speed: (" + speed.x + ","
                + speed.y + ")"));
    }

    private static void drawCanvas(int n, float limX, float limY) {
        StdDraw.setCanvasSize(1024 * n, 1024); // Size of canvas
        StdDraw.setXscale(-0.9, limX + 0.9);          // x-scale
        StdDraw.setYscale(-0.9, limY + 0.9);          // y-scale
    }

    private static void drawTrackBlack(float limX, float limY) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(limX / 2, limY / 2, limX / 2, limY / 2);
    }

    private static void drawGridLines(float limX, float limY) {
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY); // Grid color
        StdDraw.setPenRadius(0.002);             // Grid thickness
        for (int i = 0; i < limX; i++) {
            StdDraw.line(i, limY, i, 0); // Vertical lines
            StdDraw.line(limX, i, 0, i); // Horizontal lines
        }
    }

    private static void drawFinishLine(float limX, float limY) {
        StdDraw.setPenRadius(0.01);                // Line thickness
        StdDraw.setPenColor(StdDraw.WHITE);        // Line color
        StdDraw.line(limX / 2, limY / 2,
                limX / 2, limY);               // Draw
    }

    private static void drawMiddleWhite(float limX, float limY) {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(limX / 2, limY / 2, limX / 4, limY / 4);
    }

    private static void drawTrackEdges(float limX, float limY) {
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        StdDraw.rectangle(limX / 2, limY / 2, limX / 2, limY / 2);
        StdDraw.rectangle(limX / 2, limY / 2, limX / 4, limY / 4);
    }

    // Draws either the rectangular og square track
    private static void drawTrack(float limX, float limY, int n) {
        drawCanvas(n, limX, limY);   // Defines canvas
        drawTrackBlack(limX, limY);  // Fills with black
        drawGridLines(limX, limY);   // Grid lines
        drawFinishLine(limX, limY);  // Finish line
        drawMiddleWhite(limX, limY); // White middle
        drawTrackEdges(limX, limY);  // Track edges
    }

    // Checks the direction the user takes around the track
    private static boolean detectClockwise(boolean clockwise, boolean checkpoint, Point pos, int limX) {
        if (!clockwise && !checkpoint && pos.x > limX / 2) {
            clockwise = true;
        }
        return clockwise;
    }

    // Checks if the user has been in the bottom middle of the map
    private static boolean detectCheckpoint(boolean checkpoint, Point pos, int limX, int limY) {
        if (!checkpoint && pos.y > 0 && pos.y < limY / 2 && pos.x >= limX / 4 && pos.x <= limX / 4 * 3) {
            checkpoint = true;
        }
        return checkpoint;
    }

    private static void detectRaceFinish(int moves, float limX, float limY, Point pos, boolean clockwise, boolean checkpoint) {
        if ((clockwise && checkpoint && pos.x >= limX/2 && pos.y >= 3./4 * limY && pos.y <= limY) ||
                (!clockwise && checkpoint && pos.x <= limX / 2 && pos.y >= limY / 2 && pos.y <= limY)) {
            System.out.println("\nFINISHED!!! \nYou've used '" + moves + "' moves to finish the track!");
            exitMessage();
        }
    }

    // Detects if user crashes and asks if user wants to try again.
    private static boolean detectUserCrash(int limX, int limY, Point pos, Scanner console, int track) {
        String retry;
        if ((0 >= pos.x || pos.x >= limX) || (0 >= pos.y || pos.y >= limY)
                || (1. / 4 * limX <= pos.x && pos.x <= 3. / 4 * limX
                && 1. / 4 * limY <= pos.y && pos.y <= 3. / 4 * limY)) {

            System.out.print("\nYou've crashed! :("); // Crash message
            retry = getRetryAnswer(console); // Loads method

            // Redraws map if user wants to try again
            if (retry.equals("yes")) {
                StdDraw.clear();
                deleteFiles();
                if (track == 1) {
                    drawTrack(limX,limY,track);
                } else {
                    drawTrack(limX,limY,track);
                }
                return true;    // Returns true if user wants to try again
            } else if (retry.equals("no")) {
                exitMessage();  // Exits program if user doesn't want to try again
            }
        }
        return false; // False when not crashed
    }
}
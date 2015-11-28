package walker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Walker v.1.0
 * Created by spasynkov on 27.10.2015.
 */
public class walker {
    private static int x = 0;       // current x position
    private static int y = 0;       // current y position
    private static int maxX;        // maximum size of field (by x)
    private static int maxY;        // maximum size of field (by y)
    private static int startX;      // starting point for x
    private static int startY;      // starting point for y

    private static boolean couldWeMove;     // if we going to move by the edge of field
    private static long stepsCounter = 0;   // the number of moves counter
    private static boolean spectatorMode;   // if program will move by its own
    private static String direction;        // direction where we going to move

    private static ArrayList<int[]> allXY = new ArrayList<>();  // list of all points (x, y)

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        System.out.println("Enter field size.");
        boolean repeat = true;
        while (repeat) {
            repeat = false;
            System.out.print("x (max) = ");
            try {
                maxX = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                repeat = true;
            }
            if (maxX <= 0 || repeat) {
                System.out.println("Please enter the size of field for x (number grater than 0)");
                repeat = true;
            }
        }
        repeat = true;

        while (repeat) {
            repeat = false;
            System.out.print("y (max) = ");
            try {
                maxY = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                repeat = true;
            }
            if (maxY <= 0 || repeat) {
                System.out.println("Please enter the size of field for y (number grater than 0)");
                repeat = true;
            }
        }
        repeat = true;

        System.out.println("Enter starting position");
        while (repeat) {
            repeat = false;
            System.out.print("x = ");
            try {
                startX = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                repeat = true;
            }
            if (startX < 0 || repeat) {
                System.out.println("Please enter the starting point coordinate x (number not less than 0)");
                repeat = true;
            }
        }
        repeat = true;

        while (repeat) {
            repeat = false;
            System.out.print("y = ");
            try {
                startY = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                repeat = true;
            }
            if (startY < 0 || repeat) {
                System.out.println("Please enter the starting point coordinate y (number not less than 0)");
                repeat = true;
            }
        }
        repeat = true;

        allXY.add(new int[]{startX, startY});       // adding starting point

        System.out.println("You could move by your own with numpad keys (left - 4, right - 6, up - 8, down - 2, etc).");
        System.out.println("Or you could run program in \"spectator mode\".");
        System.out.print("Do you want to spectate? [y/n] ");
        String answer = reader.readLine();
        if ("yes".equals(answer.toLowerCase()) || "y".equals(answer.toLowerCase()))
            spectatorMode = true;

        int steps = 0;
        System.out.println("You may specify the number of steps should be made, or enter 0 for infinite loop.");
        System.out.println("But in such case you will need to terminate program, so you will not get any statistics.");
        while (repeat) {
            repeat = false;
            System.out.print("How many steps? ");
            try {
                steps = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                repeat = true;
            }
            if (steps < 0 || repeat) {
                System.out.println("Please enter the number of steps you want to do (not less than 0) ");
                repeat = true;
            }
        }

        start(steps);

        drawStatistics();

        System.out.print("Thank you for using this program. Press Enter to exit...");
        reader.readLine();
        reader.close();
    }

    private static void drawStatistics() {
        System.out.println("--------------------------------------------------");
        System.out.println("Finished now!");
        System.out.println(stepsCounter + " steps were made.");
        System.out.println();
        int bestX = maxX, bestY = maxY;
        if (startX > maxX) bestX = startX;
        if (startY > maxY) bestY = startY;

        removeDuplicates();

        // searching mins and maxes
        int minXResults = allXY.get(0)[0], maxXResults = allXY.get(0)[0], minYResults = allXY.get(0)[1], maxYResults = allXY.get(0)[1];
        for (int[] results: allXY){
            if (results[0] < minXResults) minXResults = results[0];
            if (results[0] > maxXResults) maxXResults = results[0];
            if (results[1] < minYResults) minYResults = results[1];
            if (results[1] > maxYResults) maxYResults = results[1];
        }

        // printing the table of points we have
        for (int i = 0; i <= bestY; i++) {
            for (int j = 0; j <= bestX; j++) {
                boolean found = false;
                for (int[] results : allXY) {
                    if (results[0] == j && results[1] == i) {
                        System.out.print(" 8 ");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    if (i > maxY || j > maxX) {
                        System.out.print("   ");
                    } else System.out.print(" . ");
                }
            }
            System.out.println();
        }
        System.out.println();

        int square = allXY.size();  // total number of unique points in figure - is the square of the figure

        System.out.println("Total square covered is " + square + ".");
        System.out.println();
        System.out.println("X covered: from " + minXResults + " to " + maxXResults + ". (" + (maxXResults - minXResults + 1) + ")");
        System.out.println("Y covered: from " + minYResults + " to " + maxYResults + ". (" + (maxYResults - minYResults + 1) + ")");

        String space = " ";
        for (int i = 0; i < String.valueOf(minXResults).length(); i++) {
            space += " ";
        }
        System.out.println(space + minYResults);
        System.out.println(minXResults + " + " + maxXResults);
        System.out.println(space + maxYResults);
    }

    private static void removeDuplicates() {
        ArrayList<int[]> tmp = new ArrayList<>();
        tmp.add(allXY.get(0));
        for (int[] ints: allXY) {
            boolean add = false;
            for (int[] newInts: tmp) {
                add = true;
                if (ints[0] == newInts[0] && ints[1] == newInts[1]) {
                    add = false;
                    break;
                }
            }
            if (add) tmp.add(ints);
        }
        allXY = tmp;
    }

    private static void calculateSquare1(int minX, int maxX, int minY, int maxY) {
        // creating list of results
        // y1: x1, x2, x3, ...
        // y2: x1, x2, x3, ...
        Map<Integer, Integer[]> results = new HashMap<>();
        ArrayList<Integer> tmp;
        for (int i = minY; i < maxY; i++) {
            tmp = new ArrayList<>();
            for (int j = minX; j < maxX; j++) {
                for (int[] res: allXY) {
                    if (res[0] == j && res[1] == i) {
                        tmp.add(j);
                    }
                }
            }
            if (tmp.size() > 0) {
                Collections.sort(tmp);
                results.put(i, tmp.toArray(new Integer[tmp.size()]));
            }
        }

        for (Map.Entry<Integer, Integer[]> x: results.entrySet()) {
            System.out.print(x.getKey() + ": ");
            for (Integer xx: x.getValue())
                System.out.print(xx + " ");
            System.out.println();
        }
    }

    private static void start(int steps) {
        x = startX;
        y = startY;

        int counter = 0;
        if (steps == 0) counter--;
        while (counter < steps){
            move();
            printPosition();
            if (steps != 0) counter++;

            try {
                Thread.sleep(500l);
            } catch (InterruptedException e) {
                System.out.println("There was an error while processing sleep.");
                e.printStackTrace();
            }
        }
    }

    private static void printPosition() {
        System.out.println("Step " + ++stepsCounter + ":\nx: " + x + ", y: " + y + "\n");
    }

    private static void move() {
        do {
            int moveTo;
            if (spectatorMode) moveTo = ((int) (Math.random() * 8)) % 8;
            else moveTo = askUserWhereToGo();

            switch (moveTo) {
                case 0: {
                    direction = "up";
                    moveUp();
                    break;
                }
                case 1: {
                    direction = "up and right";
                    moveUpAndRight();
                    break;
                }
                case 2: {
                    direction = "right";
                    moveRight();
                    break;
                }
                case 3: {
                    direction = "down and right";
                    moveDownAndRight();
                    break;
                }
                case 4: {
                    direction = "down";
                    moveDown();
                    break;
                }
                case 5: {
                    direction = "down and left";
                    moveDownAndLeft();
                    break;
                }
                case 6: {
                    direction = "left";
                    moveLeft();
                    break;
                }
                case 7: {
                    direction = "up and left";
                    moveUpAndLeft();
                    break;
                }
                default:
                    System.out.println("You will never see this :)");
            }
        } while (!couldWeMove);
        allXY.add(new int[] {x, y});
    }

    private static int askUserWhereToGo() {
        int directionNumber = 0;
        System.out.print("Your move: ");
        do {
            try {
                directionNumber = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                System.out.print("Error while input. Please repeat.\nEnter number (1..9): ");
            }
        } while (directionNumber < 0 || directionNumber > 10);
        switch (directionNumber){
            case 8: return 0;
            case 9: return 1;
            case 6: return 2;
            case 3: return 3;
            case 2: return 4;
            case 1: return 5;
            case 4: return 6;
            case 7: return 7;
        }
        return 4;   // same as "2" pressed
    }

    private static void moveUpAndLeft() {
        couldWeMove = checkUp() && checkLeft();
        if (couldWeMove) {
            System.out.println("Moving " + direction + "...");
            x -= 1;
            y -= 1;
        } else if (!spectatorMode) System.out.println("Can't move " + direction + " from this position!");
    }

    private static void moveLeft() {
        couldWeMove = checkLeft();
        if (couldWeMove) {
            System.out.println("Moving " + direction + "...");
            x -= 1;
        }
        else if (!spectatorMode) System.out.println("Can't move " + direction + " from this position!");
    }

    private static void moveDownAndLeft() {
        couldWeMove = checkDown() && checkLeft();
        if (couldWeMove) {
            System.out.println("Moving " + direction + "...");
            x -= 1;
            y += 1;
        } else if (!spectatorMode) System.out.println("Can't move " + direction + " from this position!");
    }

    private static void moveDown() {
        couldWeMove = checkDown();
        if (couldWeMove) {
            System.out.println("Moving " + direction + "...");
            y += 1;
        }
        else if (!spectatorMode) System.out.println("Can't move " + direction + " from this position!");
    }

    private static void moveDownAndRight() {
        couldWeMove = checkDown() && checkRight();
        if (couldWeMove) {
            System.out.println("Moving " + direction + "...");
            x += 1;
            y += 1;
        } else if (!spectatorMode) System.out.println("Can't move " + direction + " from this position!");
    }

    private static void moveRight() {
        couldWeMove = checkRight();
        if (couldWeMove) {
            System.out.println("Moving " + direction + "...");
            x += 1;
        }
        else if (!spectatorMode) System.out.println("Can't move " + direction + " from this position!");
    }

    private static void moveUpAndRight() {
        couldWeMove = checkUp() && checkRight();
        if (couldWeMove) {
            System.out.println("Moving " + direction + "...");
            x += 1;
            y -= 1;
        } else if (!spectatorMode) System.out.println("Can't move " + direction + " from this position!");
    }

    private static void moveUp() {
        couldWeMove = checkUp();
        if (couldWeMove) {
            System.out.println("Moving " + direction + "...");
            y -= 1;
        }
        else if (!spectatorMode) System.out.println("Can't move " + direction + " from this position!");
    }

    private static boolean checkUp() {
        return (y > 0);
    }
    private static boolean checkDown() {
        return (y < maxY);
    }
    private static boolean checkRight() {
        return (x < maxX);
    }
    private static boolean checkLeft() {
        return (x > 0);
    }
}

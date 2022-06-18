package model;

import java.security.SecureRandom;

/*This class is responsible for:
- initiating the game,
- reading the file,
- loading the settings,
- interacting with the other classes,
- writing to the file when the game ends.

*/
public class Game {
    private final ShipList playerShips = new ShipList();
    private final ShipList computerShips = new ShipList();

    public void start() throws InterruptedException {
//      DOWNLOAD SETTINGS
        String[] gameSettings = FileIO.loadGameSettings();
        int gridSize = Integer.parseInt(gameSettings[0]);
        boolean multipleHitsAllowed = Boolean.parseBoolean(gameSettings[1]);
        boolean computerShipsVisible = Boolean.parseBoolean(gameSettings[2]);
        int numOfShips = Integer.parseInt(gameSettings[3]);
        boolean manualUserShipCreation = Boolean.parseBoolean(gameSettings[4]);

//      WELCOME
        printWelcome(gridSize, multipleHitsAllowed, computerShipsVisible, numOfShips, manualUserShipCreation);
        System.out.println("Press any key to continue...");
        Input.readString();

        //INITIALIZING SHIPS
        CoordinateGenerator coordinateGenerator = new CoordinateGenerator(gridSize);
        SecureRandom random = new SecureRandom();
        System.out.println("Now we need to create your fleet and put it on the map.");
        createShips(numOfShips, random, gridSize, multipleHitsAllowed, manualUserShipCreation, coordinateGenerator);

//        Ship[][] playerGrid; Сделать таким образом.
        String[][] playerGrid = gridInitialization(gridSize);
        String[][] shotsMap = gridInitialization(gridSize);
        String[][] computerGrid = gridInitialization(gridSize);

//      SHOW USER PC SHIPS
        if (computerShipsVisible) {
            System.out.println("Computer fleet");
            placeShipsOnGrid(computerGrid, computerShips);
            printGrid(computerGrid);
            System.out.println("_".repeat(12));
        }

        //GAME
        int round = 1;
        int playerScore = 0;
        int computerScore = 0;
        Ship shipByCoordinates = null;
        int x;
        int y;
        boolean userWon = false;
        while (!isGameOver()) {
            System.out.println("Round " + round++);
            System.out.println("Player score: " + playerScore);
            System.out.println("Computer score: " + computerScore);
            System.out.println("Player grid");
            placeShipsOnGrid(playerGrid, playerShips);
            printGrid(playerGrid);
            System.out.println("-".repeat(12));
            printGrid(shotsMap);

            System.out.println("YOUR TURN");
            System.out.println("Ship X position (0 - " + (gridSize - 1) + ")");
            x = Input.readInt();
            System.out.println("Ship Y position (0 - " + (gridSize - 1) + ")");
            y = Input.readInt();
            if (x < 0 || y < 0 || x >= gridSize || y >= gridSize) {
                System.out.println("The projectile was fired, but fell somewhere far, far away...");
            } else {
                shipByCoordinates = computerShips.getShipByCoordinates(x, y);
                if (shipByCoordinates == null) {
                    System.out.println("YOU MISS!!!");
                    shotsMap[y][x] = "*";
                    Thread.sleep(1000);
                } else {
                    if (shipByCoordinates.isAlive()) {
                        System.out.println("YOU HITTTTT!!!");
                        playerScore += 10;
                        Thread.sleep(1000);
                        shipByCoordinates.increaseHits();
                        shotsMap[y][x] = shipByCoordinates.getStatus();
                        if(shipByCoordinates.getStatus().equals("X")){
                            System.out.println("Well done!!! You destroyed enemy's ship!");
                            Thread.sleep(1000);
                        }
                        if (isGameOver()){
                            System.out.println("CONGRATULATIONS!!! YOU WON!");
                            userWon = true;
                            break;
                        }
                    } else {
                        System.out.println("This ship has already been destroyed.");
                        Thread.sleep(1000);
                    }
                }
            }

            System.out.println("COMPUTER'S TURN");
            Thread.sleep(1000);
            x = coordinateGenerator.generateInt();
            y = coordinateGenerator.generateInt();
            System.out.println("Computer guesses X: " + x);
            Thread.sleep(1000);
            System.out.println("Computer guesses y: " + y);
            Thread.sleep(1000);
            shipByCoordinates = playerShips.getShipByCoordinates(x, y);
            if (shipByCoordinates == null) {
                System.out.println("COMPUTER MISS!");
                Thread.sleep(1000);
            } else {
                if (shipByCoordinates.isAlive()) {
                    System.out.println("COMPUTER HITTTTT!!!");
                    computerScore += 10;
                    shipByCoordinates.increaseHits();
                    switch (shipByCoordinates.getStatus()){
                        case "D" -> System.out.println("Sir, " + shipByCoordinates.getShipName() + " is under attack!!!");
                        case "X" -> System.out.println("Sir, " + shipByCoordinates.getShipName() + " has been destroyed!!!");
                        default -> System.out.println();
                    }
                    Thread.sleep(1500);
                    if (isGameOver()){
                        System.out.println("OH NO! YOUR FLEET HAS BEEN DESTROYED!");
                        break;
                    }
                } else {
                    System.out.println("This ship has already been destroyed.");
                    Thread.sleep(1000);
                }
            }
            System.out.println("=".repeat(20));
        }

        if (userWon) FileIO.saveResults(playerScore, computerScore);
    }

    private void createShips(int numOfShips, SecureRandom random, int gridSize, boolean multipleHitsAllowed, boolean manualUserShipCreation, CoordinateGenerator coordinateGenerator) {
        for (int i = 0; i < numOfShips; i++) {
            if (manualUserShipCreation) {
                addUserShipManualy(playerShips, random, gridSize, multipleHitsAllowed, i);
            } else {
                addRandomShip(playerShips, random, coordinateGenerator, multipleHitsAllowed);
            }
            addRandomShip(computerShips, random, coordinateGenerator, multipleHitsAllowed);
        }
    }

    public boolean isGameOver() {
        boolean allUserShipsDestroyed = true;
        boolean allComputerShipsDestroyed = true;
        for (int i = 0; i < playerShips.getLen(); i++) {
            if (playerShips.getShipById(i).isAlive()) {
                allUserShipsDestroyed = false;
                break;
            }
        }
        for (int i = 0; i < computerShips.getLen(); i++) {
            if (computerShips.getShipById(i).isAlive()) {
                allComputerShipsDestroyed = false;
                break;
            }
        }
        return allUserShipsDestroyed || allComputerShipsDestroyed;
    }

    private void addUserShipManualy(ShipList playerShips, SecureRandom random, int gridSize, boolean multipleHitsAllowed, int i) {
        boolean needAddUserShip = true;
        while (needAddUserShip) {
            try {
                System.out.println("Please enter the details for the ship # " + (i + 1));
                System.out.println("Enter ship's name (3 and 15 characters).");
                String shipName = Input.readString();
                Validation.checkName(shipName, playerShips);

                System.out.println("Ship x Position (0 - " + (gridSize - 1) + "):");
                int x = Input.readInt();
                System.out.println("Ship y Position (0 - " + (gridSize - 1) + "):");
                int y = Input.readInt();
                Validation.checkCoordinates(x, y, gridSize, playerShips);

                Ship ship = new Ship(shipName, x, y, multipleHitsAllowed ? random.nextInt(1, 6) : 1, 0);
                this.playerShips.addShip(ship);
                needAddUserShip = false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void addRandomShip(ShipList list, SecureRandom random, CoordinateGenerator coordinateGenerator, boolean multipleHitsAllowed) {
        while (true) {
            try {
                String shipName = "Random Ship #" + random.nextInt(1, 1000);

                int x = coordinateGenerator.generateInt();
                int y = coordinateGenerator.generateInt();
                int hullStrength = multipleHitsAllowed ? random.nextInt(1, 6) : 1;
                Ship ship = new Ship(shipName, x, y, hullStrength, 0);
                if (!list.contains(ship)) {
                    list.addShip(ship);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void printGrid(String[][] grid) {
        System.out.print("  ");
        for (int i = 0; i < grid.length; i++) {
            System.out.print(i + "\s");
        }
        System.out.println();
        for (int y = 0; y < grid.length; y++) {
            System.out.print(y + "\s");
            for (int x = 0; x < grid[y].length; x++) {
                System.out.print(grid[y][x] + "\s");
            }
            System.out.println();
        }
    }

    private void placeShipsOnGrid(String[][] grid, ShipList list) {
        for (int i = 0; i < list.getLen(); i++) {
            int x = list.getShipById(i).getxPos();
            int y = list.getShipById(i).getyPos();
            grid[y][x] = list.getShipById(i).getStatus();
        }
    }

    private String[][] gridInitialization(int gridSize) {
        String[][] grid = new String[gridSize][gridSize];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                grid[i][j] = "~";
            }
        }
        return grid;
    }

    private void printWelcome(int gridSize, boolean multipleHitsAllowed, boolean computerShipsVisible, int numOfShips, boolean manualUserShipCreation) {
        System.out.println("+" + "=".repeat(68) + "+");
        System.out.println("|" + " ".repeat(68) + "|");
        System.out.println("|" + " ".repeat(10) + "Welcome to the Battleship Game -- With a Twist!!" + " ".repeat(10) + "|");
        System.out.println("|" + " ".repeat(68) + "|");
        System.out.println("+" + "=".repeat(68) + "+");
        System.out.println("The game will use the grid size defined in the settings file.");
        System.out.printf("Playing grid size set as (%1$s X %1$s)%n", gridSize);
        System.out.println("Maximum number of ships allowed: " + numOfShips);
        System.out.println("Multiple hits allowed per ship: " + (multipleHitsAllowed ? "YES" : "NO"));
        System.out.println("Computer ships visible " + (computerShipsVisible ? "YES" : "NO"));
        System.out.println("Manual User ship creation " + (manualUserShipCreation ? "YES" : "NO"));
    }
}

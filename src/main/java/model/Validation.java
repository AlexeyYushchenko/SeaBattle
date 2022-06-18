package model;

public class Validation {

    private Validation(){}

    public static void checkName(String shipName, ShipList playerShips) throws IllegalArgumentException {
        if (shipName.length() < 3 || shipName.length() > 15) {
            System.out.println("Can't name your ship with such a name: " + shipName);
            throw new IllegalArgumentException();
        }else if (playerShips.contains(shipName)){
            System.out.println("Your fleet already has a ship with such name");
            throw new IllegalArgumentException();
        }
    }

    public static void checkCoordinates(int x, int y, int gridSize, ShipList playerShips) {
        if (x < 0 || x >= gridSize) throw new IllegalArgumentException("X coordinate is incorrect.");
        if (y < 0 || y >= gridSize) throw new IllegalArgumentException("Y coordinate is incorrect.");

        if (playerShips.contains(x, y)){
            throw new IllegalArgumentException("We already have a ship with such coordinates.");
        }
    }
}

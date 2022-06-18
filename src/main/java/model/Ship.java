package model;

import java.util.Objects;

public class Ship {
    private String shipName;
    private int xPos;
    private int yPos;
    private int numOfHitsNeeded;
    private int numOfHitsMade;

    public Ship(String shipName, int xPos, int yPos, int numOfHitsNeeded, int numOfHitsMade) {
        this.shipName = shipName;
        this.xPos = xPos;
        this.yPos = yPos;
        this.numOfHitsNeeded = numOfHitsNeeded;
        this.numOfHitsMade = numOfHitsMade;
    }

    public String getStatus() {
        if (numOfHitsMade == 0) return "O";
        if (numOfHitsMade < numOfHitsNeeded) return "D";
        return "X";
    }

    public boolean isAlive() {
        return numOfHitsMade < numOfHitsNeeded;
    }

    public String getShipName() {
        return shipName;
    }

    public int getxPos() {
        return xPos;
    }


    public int getyPos() {
        return yPos;
    }

    public void increaseHits() {
        this.numOfHitsMade++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return xPos == ship.xPos && yPos == ship.yPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPos, yPos);
    }

    @Override
    public String toString() {
        return "Ship{" +
                "shipName='" + shipName + '\'' +
                ", xPos=" + xPos +
                ", yPos=" + yPos +
                ", numOfHitsNeeded=" + numOfHitsNeeded +
                ", numOfHitsMade=" + numOfHitsMade +
                '}';
    }
}

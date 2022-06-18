package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class ShipList {
    private List<Ship> list;

    public ShipList() {
        this.list = new ArrayList<>();
    }

    public boolean addShip(Ship ship) {
        return this.list.add(ship);
    }

    public boolean contains(Ship other) {
        for (Ship ship : list) {
            if (ship.getShipName().equalsIgnoreCase(other.getShipName())) {
                return true;
            }
            if (ship.getxPos() == other.getxPos() && ship.getyPos() == other.getyPos()) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(String shipName) {
        for (Ship ship : list) {
            if (ship.getShipName().equalsIgnoreCase(shipName)) {
                return true;
            }
        }
        return false;
    }

    public int getLen() {
        return list.size();
    }

    public Ship getShipById(int i) {
        return this.list.get(i);
    }

    public Ship getShipByCoordinates(int x, int y) {
        return this.list.stream()
                .filter(ship -> ship.getyPos() == y && ship.getxPos() == x)
                .findAny().orElse(null);
    }

    public boolean contains(int x, int y) {
        return getShipByCoordinates(x, y) != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipList shipList = (ShipList) o;
        return Objects.equals(list, shipList.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\n");
        for (Ship ship : list) {
            sj.add(ship.toString());
        }
        return "ShipList{" +
                "list=\n" + sj +
                '}';
    }
}

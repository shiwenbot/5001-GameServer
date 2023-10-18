package Board;

import Animal.Animal;

import java.util.HashMap;

public class Data{
    public static class Coordinate {
        private int row;
        private int col;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public void setX(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setY(int col) {
            this.col = col;
        }
    }

    public static HashMap<Animal, Coordinate> objectPosition = new HashMap<>();

    public void put(Animal animal, Coordinate coordinate) {
        objectPosition.put(animal, coordinate);
    }

    public  Coordinate get(String key) {
        return objectPosition.get(key);
    }
}

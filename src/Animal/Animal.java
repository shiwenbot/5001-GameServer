package Animal;

import Board.*;


import static Board.Data.objectPosition;

public class Animal {
    private String name;

    public Animal(String name) {
        this.name = name;
        System.out.println("Construct an animal.");
    }

    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        return true;
    }

    public Square getSquare(Animal animal){
        Game game = Game.getInstance(1);
        Data.Coordinate coordinate = objectPosition.get(animal);
        int row = coordinate.getRow();
        int col = coordinate.getCol();
        return game.getSquare(row, col);
    }
}

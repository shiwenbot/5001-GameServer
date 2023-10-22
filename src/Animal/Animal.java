package Animal;

import Board.*;

import static Board.Game.objectPosition;


public class Animal {
    private String name;
    private int lifePoints;

    public Animal(String name) {
        this.name = name;
        //System.out.println("Construct an animal.");
    }

    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        return true;
    }

    public Square getSquare(Animal animal){
        Game game = Game.getInstance(1);
        Coordinate coordinate = objectPosition.get(animal);
        int row = coordinate.getRow();
        int col = coordinate.getCol();
        return game.getSquare(row, col);
    }

    public String getName(){
        return this.name;
    }

    public void heal(){
        this.lifePoints += 10;
    }
}

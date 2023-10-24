package Animal;

import Board.*;

import java.util.HashMap;
import java.util.Map;

import static Board.Game.objectPosition;


public class Animal {
    public String name;
    public Map<Spell, Integer> spells = new HashMap<>();
    public int lifePoints;
    public String description;

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

    public String getDescription() {
        return null;
    }
    public Map<Spell, Integer> getSpells() {
        return spells;
    }

    public int getLifePoints() {
        return lifePoints;
    }
}

package Animal;

import Board.Coordinate;
import Board.Game;
import Board.Spell;
import Board.Square;

import java.util.HashMap;
import java.util.Map;


import static Board.Game.objectPosition;
import static Server.GameServerMain.seed;


public class Animal {
    public String name;
    public String type = "Animal";

    public HashMap<Spell, Integer> spells = new HashMap<>();
    public boolean moveable;
    public boolean spellable;
    public int lifePoints;

    public String description;
    public Animal(String name) {
        this.name = name;
    }
    public void setSpells(Map<Spell, Integer> spells, Spell spell) {
        spells.put(spell, spells.getOrDefault(spell, 0) + 1);
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }
    public String getType() {
        return type;
    }
    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }
    public void setSpellable(boolean spellable) {
        this.spellable = spellable;
    }
    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        return true;
    }

    public Square getSquare(Animal animal){
        Game game = Game.getInstance(seed);
        Coordinate coordinate = objectPosition.get(animal);
        int row = coordinate.getRow();
        int col = coordinate.getCol();
        return game.getSquare(row, col);
    }
    public boolean isMoveable() {
        return moveable;
    }

    public boolean isSpellable() {
        return spellable;
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
    public boolean withCreature(int row, int col){
        Game game = Game.getInstance(seed);
        if(game.getSquare(row, col).isHasCreature()) return true;
        return false;
    }
    public boolean isStraightLine(int oldRow, int oldCol, int newRow, int newCol){
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);
        if((oldRow == newRow) || (oldCol == newCol) || (rowMovement == colMovement)){
            return true;
        }
        return false;
    }
}

package Animal;

import Board.Game;
import Board.Spell;

import java.util.Map;

import static Board.Game.errorMessage;
import static Server.GameServerMain.seed;

public class Owl extends Animal {
    public int lifePoints = 100;
    public String description = "The owl has wings. The owl has prescription contact lenses but cannot put them on.";
    public String type = "Animal";
    public boolean moveable = false;
    public boolean spellable = false;
    @Override
    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }
    public boolean isMoveable() {
        return moveable;
    }
    public void setSpells(Map<Spell, Integer> spells, Spell spell) {
        spells.put(spell, spells.getOrDefault(spell, 0) + 1);
    }
    @Override
    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    @Override
    public void setSpellable(boolean spellable) {
        this.spellable = spellable;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
    public void heal(){
        this.lifePoints += 10;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<Spell, Integer> getSpells() {
        return spells;
    }

    @Override
    public int getLifePoints() {
        return lifePoints;
    }

    public Owl(String name) {
        super(name);
    }

    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);

        Game game = Game.getInstance(seed);
        // not straight line
        if (!isStraightLine(oldRow, oldCol, newRow, newCol)) {
            errorMessage = "The owl can only move in a straight line.";
            throw new Exception(errorMessage);
        } else if (game.board[newRow][newCol].isHasAnimal()) {
            errorMessage = "There is an animal in this square.";
            throw new Exception(errorMessage);
        }
        //fly
        else if (rowMovement > 1 || colMovement > 1) {
            //8 directions
            if (rowMovement == 0) {
                int stepsLeft = colMovement;
                if (newCol - oldCol > 0) {
                    for (int i = 1; i <= stepsLeft; i++) {
                        if (withCreature(newRow, oldCol + i) && game.getSquare(newRow, oldCol + i).isHasAnimal()) {
                            errorMessage = "The last move was invalid.";
                            throw new Exception(errorMessage);
                        } else if (withCreature(newRow, oldCol + i)) {
                            game.getSquare(newRow, oldCol + i).setAnimal(this);
                            game.getSquare(oldRow, oldCol).setHasAnimal(false);
                            return true;
                        }
                    }
                } else {
                    for (int i = 1; i <= stepsLeft; i++) {
                        if (withCreature(newRow, oldCol - i) && game.getSquare(newRow, oldCol - i).isHasAnimal()) {
                            errorMessage = "The last move was invalid.";
                            throw new Exception(errorMessage);
                        } else if (withCreature(newRow, oldCol - i)) {
                            game.getSquare(newRow, oldCol - i).setAnimal(this);
                            game.getSquare(oldRow, oldCol).setHasAnimal(false);
                            return true;
                        }
                    }
                }
                game.getSquare(newRow, newCol).setAnimal(this);
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;

            } else if (colMovement == 0) {
                int stepsLeft = rowMovement;
                if (newRow - oldRow > 0) {
                    for (int i = 1; i <= stepsLeft; i++) {
                        if (withCreature(oldRow + i, newCol) && game.getSquare(oldRow + i, newCol).isHasAnimal()) {
                            errorMessage = "The last move was invalid.";
                            throw new Exception(errorMessage);
                        } else if (withCreature(oldRow + i, newCol)) {
                            game.getSquare(oldRow + i, newCol).setAnimal(this);
                            game.getSquare(oldRow, oldCol).setHasAnimal(false);
                            return true;
                        }
                    }
                } else {
                    for (int i = 1; i <= stepsLeft; i++) {
                        if (withCreature(oldRow - i, newCol) && game.getSquare(oldRow - i, newCol).isHasAnimal()) {
                            errorMessage = "The last move was invalid.";
                            throw new Exception(errorMessage);
                        } else if (withCreature(oldRow - i, newCol)) {
                            game.getSquare(oldRow - i, newCol).setAnimal(this);
                            game.getSquare(oldRow, oldCol).setHasAnimal(false);
                            return true;
                        }
                    }
                }
                game.getSquare(newRow, newCol).setAnimal(this);
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            }
            else {
                int rowDiff = newRow - oldRow;
                int colDiff = newCol - oldCol;
                if (rowDiff > 0 && colDiff > 0){
                    int stepsLeft = colMovement;
                    for (int i = 1; i <= stepsLeft; i++){
                        if (withCreature(oldRow + i, oldCol + i) && game.getSquare(oldRow + i, oldCol + i).isHasAnimal()){
                            errorMessage = "The last move was invalid.";
                            throw new Exception(errorMessage);
                        } else if (withCreature(oldRow + i, oldCol + i)) {
                            game.getSquare(oldRow + i, oldCol + i).setAnimal(this);
                            game.getSquare(oldRow, oldCol).setHasAnimal(false);
                            return true;
                        }
                    }
                } else if (rowDiff > 0 && colDiff < 0) {
                    int stepsLeft = colMovement;
                    for (int i = 1; i <= stepsLeft; i++){
                        if (withCreature(oldRow + i, oldCol - i) && game.getSquare(oldRow + i, oldCol - i).isHasAnimal()){
                            errorMessage = "The last move was invalid.";
                            throw new Exception(errorMessage);
                        } else if (withCreature(oldRow + i, oldCol - i)) {
                            game.getSquare(oldRow + i, oldCol - i).setAnimal(this);
                            game.getSquare(oldRow, oldCol).setHasAnimal(false);
                            return true;
                        }
                    }
                } else if (rowDiff < 0 && colDiff > 0) {
                    int stepsLeft = colMovement;
                    for (int i = 1; i <= stepsLeft; i++){
                        if (withCreature(oldRow - i, oldCol + i) && game.getSquare(oldRow - i, oldCol + i).isHasAnimal()){
                            errorMessage = "The last move was invalid.";
                            throw new Exception(errorMessage);
                        } else if (withCreature(oldRow - i, oldCol + i)) {
                            game.getSquare(oldRow - i, oldCol + i).setAnimal(this);
                            game.getSquare(oldRow, oldCol).setHasAnimal(false);
                            return true;
                        }
                    }
                }else if (rowDiff < 0 && colDiff < 0){
                    int stepsLeft = colMovement;
                    for (int i = 1; i <= stepsLeft; i++){
                        if (withCreature(oldRow - i, oldCol - i) && game.getSquare(oldRow - i, oldCol - i).isHasAnimal()){
                            errorMessage = "The last move was invalid.";
                            throw new Exception(errorMessage);
                        } else if (withCreature(oldRow - i, oldCol - i)) {
                            game.getSquare(oldRow - i, oldCol - i).setAnimal(this);
                            game.getSquare(oldRow, oldCol).setHasAnimal(false);
                            return true;
                        }
                    }
                }
                game.getSquare(newRow, newCol).setAnimal(this);
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            }
        }
        //walk
        else if(rowMovement == 1 || colMovement == 1){
            game.getSquare(newRow, newCol).setAnimal(this);
            game.getSquare(oldRow, oldCol).setHasAnimal(false);
            return true;
        }
        return false;
    }
}

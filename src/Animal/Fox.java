package Animal;

import Board.Game;
import Board.Spell;

import java.util.Map;

import static Board.Game.errorMessage;
import static Server.GameServerMain.seed;

public class Fox extends Animal {
    public int lifePoints = 100;
    public String description = "The fox has a bushy tail. The fox really enjoys looking at butterflies in the sunlight.";
    public boolean moveable = false;
    public boolean spellable = false;

    public void setSpells(Map<Spell, Integer> spells, Spell spell) {
        spells.put(spell, spells.getOrDefault(spell, 0) + 1);
    }
    public boolean isMoveable() {
        return moveable;
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

    @Override
    public String getName() {
        return name;
    }
    public void heal(){
        this.lifePoints += 10;
    }
    @Override
    public Map<Spell, Integer> getSpells() {
        return spells;
    }

    @Override
    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public Fox(String name) {
        super(name);
    }

    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);

        Game game = Game.getInstance(seed);
        // Not moving horizontally or vertically
        if (rowMovement != 0 && colMovement != 0) {
            errorMessage = "The fox can only move horizontally or vertically.";
            throw new Exception(errorMessage);
        }
        // out of range
        else if (rowMovement > 3 || colMovement > 3) {
            errorMessage = "The fox can only move 3 spaces horizontally or vertically.";
            throw new Exception(errorMessage);
        } else if (game.board[newRow][newCol].isHasAnimal()) {
            errorMessage = "The fox cannot move because there is an animal in the way.";
            throw new Exception(errorMessage);
        }
        // jump two squares
        else if (rowMovement == 2 || colMovement == 2) {
            if (rowMovement == 0) {
                if (withCreature(newRow, oldCol + (newCol - oldCol) / 2)) {
                    game.getSquare(newRow, oldCol + (newCol - oldCol) / 2).setAnimal(this);
                } else if (game.board[newRow][oldCol + (newCol - oldCol) / 2].isHasAnimal()) {
                    errorMessage = "The fox cannot move because there is an animal in the way.";
                    throw new Exception(errorMessage);
                } else {
                    game.getSquare(newRow, newCol).setAnimal(this);
                }
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            }
            else if (colMovement == 0) {
                if (withCreature(oldRow + (newRow - oldRow) / 2, newCol)) {
                    game.getSquare(oldRow + (newRow - oldRow) / 2, newCol).setAnimal(this);
                    game.getSquare(oldRow, oldCol).setHasAnimal(false);
                } else if (game.board[oldRow + (newRow - oldRow) / 2][newCol].isHasAnimal()) {
                    errorMessage = "The fox cannot move because there is an animal in the way.";
                    throw new Exception(errorMessage);
                } else {
                    game.getSquare(newRow, newCol).setAnimal(this);
                }
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            }
        }
        //jump three squares
        else if (rowMovement == 3 || colMovement == 3) {
            if (rowMovement == 0) {
                int tmpCol = newCol - oldCol > 0 ? oldCol + 2 : oldCol - 2;
                int tmpCol1 = newCol - oldCol > 0 ? oldCol + 1 : oldCol - 1;
                if (withCreature(newRow, tmpCol)) {
                    game.getSquare(newRow, tmpCol).setAnimal(this);
                } else if (withCreature(newRow, tmpCol1)) {
                    game.getSquare(newRow, tmpCol1).setAnimal(this);
                } else if (game.board[newRow][tmpCol].isHasAnimal() || game.board[newRow][tmpCol1].isHasAnimal()) {
                    errorMessage = "The fox cannot move because there is an animal in the way.";
                    throw new Exception(errorMessage);
                } else {
                    game.getSquare(newRow, newCol).setAnimal(this);
                }
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            } else if (colMovement == 0) {
                int tmpRow = newRow - oldRow > 0 ? oldRow + 2 : oldRow - 2;
                int tmpRow1 = newRow - oldRow > 0 ? oldRow + 1 : oldRow - 1;
                if (withCreature(tmpRow, newCol)) {
                    game.getSquare(tmpRow, newCol).setAnimal(this);
                } else if (withCreature(tmpRow1, newCol)) {
                    game.getSquare(tmpRow1, newCol).setAnimal(this);
                } else if (game.board[tmpRow][newCol].isHasAnimal() || game.board[tmpRow1][newCol].isHasAnimal()) {
                    errorMessage = "The fox cannot move because there is an animal in the way.";
                    throw new Exception(errorMessage);
                } else {
                    game.getSquare(newRow, newCol).setAnimal(this);
                }
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            }
        }
        //walk
        else if (rowMovement == 1 || colMovement == 1) {
            game.getSquare(newRow, newCol).setAnimal(this);
            game.getSquare(oldRow, oldCol).setHasAnimal(false);
            return true;
        }
        return false;
    }
}

package Animal;

import Board.Game;
import Board.Spell;

import java.util.Map;

import static Board.Game.errorMessage;
import static Server.GameServerMain.seed;

public class Deer extends Animal {
    public int lifePoints = 100;
    public String description = "The deer has antlers. The deer is recently divorced and is looking for a new partner.";
    public boolean moveable = false;
    public boolean spellable = false;
    public void setSpells(Map<Spell, Integer> spells, Spell spell) {
        spells.put(spell, spells.getOrDefault(spell, 0) + 1);
    }
    public void heal(){
        this.lifePoints += 10;
    }
    @Override
    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
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
    @Override
    public Map<Spell, Integer> getSpells() {
        return spells;
    }
    @Override
    public int getLifePoints() {
        return lifePoints;
    }
    public Deer(String name) {
        super(name);
    }

    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);

        Game game = Game.getInstance(seed);
        //如果没有水平或竖直的走
        if (rowMovement > 3 || colMovement > 3) {
            errorMessage = "The deer can only move 3 spaces in any direction.";
            throw new Exception(errorMessage);
        } else if (!isStraightLine(oldRow, oldCol, newRow, newCol)) {
            errorMessage = " The deer can only move in a straight line.";
            throw new Exception(errorMessage);
        }
        //如果目标地点有动物
        else if(game.board[newRow][newCol].isHasAnimal()){
            errorMessage = "The fox cannot move because there is an animal in the way.";
            throw new Exception(errorMessage);
        }
        //跳和走
        else if (rowMovement >= 1 || colMovement >= 1) {
            game.getSquare(newRow, newCol).setAnimal(this);
            game.getSquare(oldRow, oldCol).setHasAnimal(false);
            return true;
        }
        return false;
    }
}

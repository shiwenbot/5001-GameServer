package Animal;

import Board.Game;
import Board.Spell;

import java.util.Map;

import static Board.Game.errorMessage;
import static Server.GameServerMain.seed;

public class Badger extends Animal {
    public int lifePoints = 100;
    public String description = "The badger has a black and white face. The badger is a often mistaken for a very small panda. The badger wears a t-shirt that says “I am not a panda” to combat this.";
    public boolean moveable = false;
    public boolean spellable = false;
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

    public Badger(String name) {
        super(name);
    }

    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);

        Game game = Game.getInstance(seed);
        //超出范围
        if (rowMovement > 2 || colMovement > 2) {
            errorMessage = "The badger can only move 2 spaces in a straight line.";
            throw new Exception(errorMessage);
        } else if (!isStraightLine(oldRow, oldCol, newRow, newCol)) {
            errorMessage = "The badger can only move in a straight line.";
            throw new Exception(errorMessage);
        } else if (game.board[newRow][newCol].isHasAnimal()) {
            errorMessage = "There is an animal in this square.";
            throw new Exception(errorMessage);
        }
        //挖
        else if (rowMovement == 2 || colMovement == 2) {
            if (rowMovement == 0) {
                game.getSquare(newRow, newCol).setAnimal(this);
                game.getSquare(oldRow, oldCol).setHasAnimal(false);//移动成功后之前的格子就没有动物了
                return true;
            } else if (colMovement == 0) {
                game.getSquare(newRow, newCol).setAnimal(this);
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            } else {
                game.getSquare(newRow, newCol).setAnimal(this);
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            }
        }
        //走
        else if(rowMovement == 1 || colMovement == 1){
            game.getSquare(newRow, newCol).setAnimal(this);
            game.getSquare(oldRow, oldCol).setHasAnimal(false);
            return true;
        }
        return false;
    }
}
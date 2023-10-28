package Animal;

import Board.Game;
import Board.Spell;

import java.util.HashMap;
import java.util.Map;

import static Server.GameServerMain.seed;
import static Board.Game.errorMessage;

public class Rabbit extends Animal {
    public String name = "Rabbit";
    public String type = "Animal";
    public int lifePoints = 100;
    public Map<Spell, Integer> spells = new HashMap<>();
    public String description = "The rabbit has fluffy ears and tail. The rabbit really likes to eat grass.";
    public boolean moveable = true;
    public boolean spellable = true;
    public void setSpells(Map<Spell, Integer> spells, Spell spell) {
        spells.put(spell, spells.getOrDefault(spell, 0) + 1);
    }
    @Override
    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
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
    public boolean isMoveable() {
        return moveable;
    }
    @Override
    public boolean isSpellable() {
        return spellable;
    }


    public void heal(){
        this.lifePoints += 10;
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

    // 私有构造方法，防止外部实例化
    public Rabbit(String name) {
        super(name);
    }

    /**
     * 1.可以走一步或者跳两步，
     * 不可以跳过怪物和动物，也就是说有可能错过宝物
     * 如果跳过的格子是怪物，会强制让兔子和怪物在一个格子
     * 如果是动物的话就跳失败了，留在原地
     * <p>
     * 2.Exception需要包括
     * 移动了超过两步，不是直线，越界？？？
     */
    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);

        Game game = Game.getInstance(seed);
        //超出范围
        if (rowMovement > 2 || colMovement > 2) {
            errorMessage = "The rabbit can only move 2 spaces in a straight line.";
            throw new Exception(errorMessage);
        }
        //不走直线
        else if (!isStraightLine(oldRow, oldCol, newRow, newCol)) {
            errorMessage = "The rabbit can only move in a straight line.";
            throw new Exception(errorMessage);
        } else if (game.board[newRow][newCol].isHasAnimal()) {
            errorMessage = "There is an animal in this square.";
            throw new Exception(errorMessage);
        }
        //跳
        else if (rowMovement == 2 || colMovement == 2) {
            if (rowMovement == 0) {
                //如果怪物在第一步
                //八个方向要一个一个判断吗？可以简化到判断3次，判断rowMovement，colMovement是否为0
                if (withCreature(newRow, oldCol + (newCol - oldCol) / 2)) {
                    game.getSquare(newRow, oldCol + (newCol - oldCol) / 2).setAnimal(this);
                } else if (game.getSquare(newRow, oldCol + (newCol - oldCol) / 2).isHasAnimal()) {
                    errorMessage = "The rabbit cannot move because there is an animal in the way.";
                    throw new Exception(errorMessage);
                }
                //或者第二步
                else {
                    game.getSquare(newRow, newCol).setAnimal(this);
                }
                game.getSquare(oldRow, oldCol).setHasAnimal(false);//移动成功后之前的格子就没有动物了
                return true;
            } else if (colMovement == 0) {
                if (withCreature(oldRow + (newRow - oldRow) / 2, newCol)) {
                    game.getSquare(oldRow + (newRow - oldRow) / 2, newCol).setAnimal(this);
                    game.getSquare(oldRow, oldCol).setHasAnimal(false);
                } else if (game.board[oldRow + (newRow - oldRow) / 2][newCol].isHasAnimal()) {
                    errorMessage = "The rabbit cannot move because there is an animal in the way.";
                    throw new Exception(errorMessage);
                } else {
                    game.getSquare(newRow, newCol).setAnimal(this);
                }
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            } else {
                if (withCreature(oldRow + (newRow - oldRow) / 2, oldCol + (newCol - oldCol) / 2)) {
                    game.getSquare(oldRow + (newRow - oldRow) / 2, oldCol + (newCol - oldCol) / 2).setAnimal(this);
                } else {
                    game.getSquare(newRow, newCol).setAnimal(this);
                }
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            }
        }
        //走
        else if (rowMovement == 1 || colMovement == 1) {
            game.getSquare(newRow, newCol).setAnimal(this);
            game.getSquare(oldRow, oldCol).setHasAnimal(false);
            return true;
        }
        return false;
    }


}
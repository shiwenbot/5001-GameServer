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

    // ˽�й��췽������ֹ�ⲿʵ����
    public Rabbit(String name) {
        super(name);
    }

    /**
     * 1.������һ��������������
     * ��������������Ͷ��Ҳ����˵�п��ܴ������
     * ��������ĸ����ǹ����ǿ�������Ӻ͹�����һ������
     * ����Ƕ���Ļ�����ʧ���ˣ�����ԭ��
     * <p>
     * 2.Exception��Ҫ����
     * �ƶ��˳�������������ֱ�ߣ�Խ�磿����
     */
    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);

        Game game = Game.getInstance(seed);
        //������Χ
        if (rowMovement > 2 || colMovement > 2) {
            errorMessage = "The rabbit can only move 2 spaces in a straight line.";
            throw new Exception(errorMessage);
        }
        //����ֱ��
        else if (!isStraightLine(oldRow, oldCol, newRow, newCol)) {
            errorMessage = "The rabbit can only move in a straight line.";
            throw new Exception(errorMessage);
        } else if (game.board[newRow][newCol].isHasAnimal()) {
            errorMessage = "There is an animal in this square.";
            throw new Exception(errorMessage);
        }
        //��
        else if (rowMovement == 2 || colMovement == 2) {
            if (rowMovement == 0) {
                //��������ڵ�һ��
                //�˸�����Ҫһ��һ���ж��𣿿��Լ򻯵��ж�3�Σ��ж�rowMovement��colMovement�Ƿ�Ϊ0
                if (withCreature(newRow, oldCol + (newCol - oldCol) / 2)) {
                    game.getSquare(newRow, oldCol + (newCol - oldCol) / 2).setAnimal(this);
                } else if (game.getSquare(newRow, oldCol + (newCol - oldCol) / 2).isHasAnimal()) {
                    errorMessage = "The rabbit cannot move because there is an animal in the way.";
                    throw new Exception(errorMessage);
                }
                //���ߵڶ���
                else {
                    game.getSquare(newRow, newCol).setAnimal(this);
                }
                game.getSquare(oldRow, oldCol).setHasAnimal(false);//�ƶ��ɹ���֮ǰ�ĸ��Ӿ�û�ж�����
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
        //��
        else if (rowMovement == 1 || colMovement == 1) {
            game.getSquare(newRow, newCol).setAnimal(this);
            game.getSquare(oldRow, oldCol).setHasAnimal(false);
            return true;
        }
        return false;
    }


}
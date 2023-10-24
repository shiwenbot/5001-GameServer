package Animal;

import Board.Game;
import Board.Spell;

import java.util.HashMap;
import java.util.Map;

public class Rabbit extends Animal {
    public String name = "Rabbit";
    public Map<Spell, Integer> spells = new HashMap<>();
    public String description = "The rabbit has fluffy ears and tail. The rabbit really likes to eat grass.";
    public int lifePoints = 100;

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
     *
     * 2.Exception��Ҫ����
     * �ƶ��˳�������������ֱ�ߣ�Խ�磿����
     * */
    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);
        Game game = Game.getInstance(1);
        //������Χ
        if(rowMovement > 2 || colMovement > 2){
            throw new Exception("Rabbit is trying to move to far!");
        }
        //����ֱ��
        else if(!isStraightLine(oldRow, oldCol, newRow, newCol)){
            throw new Exception("This animal is not moving by a straight line!");
        }
        //��
        else if(rowMovement == 2 || colMovement == 2){
            if(rowMovement == 0){
                //��������ڵ�һ��
                //�˸�����Ҫһ��һ���ж��𣿿��Լ򻯵��ж�3�Σ��ж�rowMovement��colMovement�Ƿ�Ϊ0
                if (withCreature(newRow, oldCol + (newCol - oldCol) / 2)){
                    game.getSquare(newRow, oldCol + (newCol - oldCol) / 2).setAnimal(this);
                }
                //���ߵڶ���
                else{
                    game.getSquare(newRow, newCol).setAnimal(this);
                }
                game.getSquare(oldRow, oldCol).setHasAnimal(false);//�ƶ��ɹ���֮ǰ�ĸ��Ӿ�û�ж�����
                return true;
            }
            else if(colMovement == 0){
                if(withCreature(oldRow + (newRow - oldRow) / 2, newCol)){
                    game.getSquare(oldRow + (newRow - oldRow) / 2, newCol).setAnimal(this);
                    game.getSquare(oldRow, oldCol).setHasAnimal(false);
                }
                else{
                    game.getSquare(newRow, newCol).setAnimal(this);
                }
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            }else{
                if (withCreature(oldRow + (newRow - oldRow) / 2, oldCol + (newCol - oldCol) / 2)){
                    game.getSquare(oldRow + (newRow - oldRow) / 2, oldCol + (newCol - oldCol) / 2).setAnimal(this);
                }
                else{
                    game.getSquare(newRow, newCol).setAnimal(this);
                }
                game.getSquare(oldRow, oldCol).setHasAnimal(false);
                return true;
            }
        }
        //��
        else if(rowMovement == 1 || colMovement == 1){

        }
        return false;
    }

    //�ж������ǲ����ߵ�ֱ��
    public boolean isStraightLine(int oldRow, int oldCol, int newRow, int newCol){
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);
        if((oldRow == newRow) || (oldCol == newCol) || (rowMovement == colMovement)){
            return true;
        }
        else return false;
    }

    //�жϸ������Ƿ��Ѿ����˹���
    private boolean withCreature(int row, int col){
        Game game = Game.getInstance(1);
        if(game.getSquare(row, col).isHasCreature()) return true;
        return false;
    }
}
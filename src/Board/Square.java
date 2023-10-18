package Board;

import Animal.*;
import Creature.*;
import Exceptions.AnimalExistsException;
import Exceptions.CreatureExistsException;

import static Board.Data.objectPosition;

public class Square {
    private int row;
    private int col;
    private Animal animal;
    private boolean hasAnimal = false;
    private boolean hasCreature = false;
    //boolean visible;
    //Creature.Creature creature;

    Square(int row, int col){
        this.row = row;
        this.col = col;
    }
    /*
    * 1.һ������ֻ����һ���������һ������͹���
    * */
    public boolean isHasAnimal(){
        return hasAnimal;
    }
    public boolean isHasCreature() {return hasCreature;}
    public void setHasAnimal(boolean bool){this.hasAnimal = bool;}
    //public void setHasCreature(boolean bool){this.hasCreature = bool;}

    //���animal�е�moveͨ���ˣ��͵�������������Ķ����λ�ã�������hashmap������
    //��ʼ����ʱ��Ҳ�����
    public void setAnimal(Animal animal) throws Exception {
        if(this.isHasAnimal()){
            throw new Exception("There is an animal in this Board.Square!");
        }
        else{
            Data.Coordinate coordinate = new Data.Coordinate(this.row, this.col);
            objectPosition.put(animal, coordinate);
            this.hasAnimal = true;
        }
    }

    void setCreature(Creature creature) throws Exception {
        if(this.isHasCreature()){
            throw new Exception("There is a creature in this Board.Square!");
        }
        else{
            creature.setCol(this.col);
            creature.setRow(this.row);
            this.hasCreature = true;
        }
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }
}
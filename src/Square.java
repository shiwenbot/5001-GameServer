import Animal.*;
import Creature.*;

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
    boolean isHasAnimal(){
        return hasAnimal;
    }
    boolean isHasCreature() {return hasCreature;}
    //���Ķ����λ�ã���col��row��ֵ
    void setAnimal(Animal animal) throws CustomExceptions.AnimalExistsException {
        if(this.isHasAnimal()){
            throw new CustomExceptions.AnimalExistsException("There is an animal in this Square!");
        }
        else{
            animal.setCol(this.col);
            animal.setRow(this.row);
            this.hasAnimal = true;
        }
    }

    void setCreature(Creature creature) throws CustomExceptions.CreatureExistsException {
        if(this.isHasCreature()){
            throw new CustomExceptions.CreatureExistsException("There is a creature in this Square!");
        }
        else{
            creature.setCol(this.col);
            creature.setRow(this.row);
            this.hasCreature = true;
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
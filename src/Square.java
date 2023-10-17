import Animal.*;
import Creature.*;
import Exceptions.AnimalExistsException;
import Exceptions.CreatureExistsException;

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
    * 1.一个格子只能有一个动物，或者一个动物和怪物
    * */
    boolean isHasAnimal(){
        return hasAnimal;
    }
    boolean isHasCreature() {return hasCreature;}
    //更改动物的位置，即col和row的值
    void setAnimal(Animal animal) throws AnimalExistsException {
        if(this.isHasAnimal()){
            throw new AnimalExistsException("There is an animal in this Square!");
        }
        else{
            animal.setCol(this.col);
            animal.setRow(this.row);
            this.hasAnimal = true;
        }
    }

    void setCreature(Creature creature) throws CreatureExistsException {
        if(this.isHasCreature()){
            throw new CreatureExistsException("There is a creature in this Square!");
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
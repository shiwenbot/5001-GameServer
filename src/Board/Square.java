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
    * 1.一个格子只能有一个动物，或者一个动物和怪物
    * */
    public boolean isHasAnimal(){
        return hasAnimal;
    }
    public boolean isHasCreature() {return hasCreature;}
    public void setHasAnimal(boolean bool){this.hasAnimal = bool;}
    //public void setHasCreature(boolean bool){this.hasCreature = bool;}

    //如果animal中的move通过了，就调用这个方法更改动物的位置，并更新hashmap的数据
    //初始化的时候也会调用
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
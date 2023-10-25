package Board;

import Animal.Animal;
import Creature.*;

import static Board.Game.objectPosition;


public class Square {
    private int row;
    private int col;
    private boolean hasAnimal = false;
    private boolean hasCreature = false;
    private boolean visible = false;

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
            Coordinate coordinate = new Coordinate(this.row, this.col);
            objectPosition.put(animal, coordinate);
            //System.out.println("Put animal in objectPosition successfully!");
            setHasAnimal(true);
        }
    }

    public void setCreature(Creature creature) throws Exception {
        if(this.isHasCreature()){
            throw new Exception("There is a creature in this Board.Square!");
        }
        else{
            Coordinate coordinate = new Coordinate(this.row, this.col);
            objectPosition.put(creature, coordinate);
            this.hasCreature = true;
            System.out.println( creature.name + "is at " + this.row + " " + this.col);
        }
    }

    public boolean isVisible(){
        return visible;
    }

    public void changeVisibile(boolean bool){
        this.visible = bool;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }
}
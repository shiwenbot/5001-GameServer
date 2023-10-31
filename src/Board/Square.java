package Board;

import Animal.Animal;
import Creature.*;

import java.util.ArrayList;

import static Board.Game.objectPosition;
import static Board.Game.spellPosition;


public class Square {
    private int row;
    private int col;
    private boolean hasAnimal = false;
    private boolean hasCreature = false;
    private boolean hasSpell = false;
    private boolean visible = false;

    Square(int row, int col){
        this.row = row;
        this.col = col;
    }
    public boolean isHasAnimal(){
        return hasAnimal;
    }
    public boolean isHasCreature() {return hasCreature;}
    public boolean isHasSpell() {return hasSpell;}
    public void setHasAnimal(boolean bool){this.hasAnimal = bool;}
    public void setHasSpell(boolean bool){this.hasSpell = bool;}
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

    /**
     * This method sets the animal in this square
     * This method will be call in move and when initialising board
     * an exception will be thrown when there is already an animal
     * */
    public void setAnimal(Animal animal) throws Exception {
        if(this.isHasAnimal()){
            throw new Exception("There is an animal in this Board.Square!");
        }
        else{
            Coordinate coordinate = new Coordinate(this.row, this.col);
            objectPosition.put(animal, coordinate);
            setHasAnimal(true);
            System.out.println( animal.name + "is at " + this.row + " " + this.col);
        }
    }

    //This method sets the creature in this square, it is called when initialising board
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

    //This method sets the spell in this square, it is called when initialising board
    public void setSpell(Spell spell) throws Exception {
        if (this.isHasSpell()){
            throw new Exception("There is a spell in this Board.Square!");
        }
        else {
            Coordinate coordinate = new Coordinate(this.row, this.col);
            spellPosition.computeIfAbsent(spell, k -> new ArrayList<>()).add(coordinate);
            this.hasSpell = true;
            System.out.println(spell.getType() + "is at " + this.row + " " + this.col);
        }
    }
}
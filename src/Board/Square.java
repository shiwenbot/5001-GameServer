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
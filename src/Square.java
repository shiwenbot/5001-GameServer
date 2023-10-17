import Animal.Animal;

public class Square {
    private int row;
    private int col;
    private Animal animal;
    private boolean hasAnimal = false;
    //boolean visible;
    //Creature.Creature creature;

    Square(int row, int col){
        this.row = row;
        this.col = col;
    }
    /*
    * 1.一个格子只能有一个动物，或者一个动物和怪物
    * */
    boolean hasAnimal(){
        return hasAnimal;
    }
    //更改动物的位置，即col和row的值
    void setAnimal(Animal animal) throws CustomExceptions.AnimalExistsException {
        if(this.hasAnimal()){
            throw new CustomExceptions.AnimalExistsException("There is an aninal in this Square!");
        }
        else{
            animal.setCol(this.col);
            animal.setRow(this.row);
            this.hasAnimal = true;
        }
    }





    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}